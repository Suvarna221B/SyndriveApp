package universe.sk.syndriveapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etBloodGroup, etDOB;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FloatingActionButton fabEdit, fabSave, fabGallery;
    CircleImageView imageView_profile_pic;
    // private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    Uri imagePath;

    public static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.profile);
        actionBar.setTitle(" Edit Profile");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.etName);
        etDOB = findViewById(R.id.etDOB);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etEmail = findViewById(R.id.etEmail);
        fabEdit = findViewById(R.id.fabEdit);
        fabSave = findViewById(R.id.fabSave);
        fabGallery = findViewById(R.id.fabGallery);
        imageView_profile_pic = findViewById(R.id.imageView_profile_pic);

        etName.setEnabled(false);
        etDOB.setEnabled(false);
        etBloodGroup.setEnabled(false);
        etEmail.setEnabled(false);
        fabSave.setEnabled(false);

        // mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);         //upload progress dialog

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
        setProfilePic();

        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userinfo userinfo;
                userinfo = dataSnapshot.getValue(Userinfo.class);

                etName.setText(userinfo.getUsername());
                etBloodGroup.setText(userinfo.getBloodgroup());
                etDOB.setText(userinfo.getUdate());
                etEmail.setText(userinfo.getUemail());
                // imageView_profile_pic.setImageURI(userinfo.getImageUri());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        //Edit User Profile - fabEdit
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setEnabled(true);
                etDOB.setEnabled(true);
                etBloodGroup.setEnabled(true);
                fabSave.setEnabled(true);
            }
        }); //end of fabEdit
        //Save User Profile into Firebase - fabSave
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String bloodgroup = etBloodGroup.getText().toString();
                String date = etDOB.getText().toString();

                Userinfo userinfo = new Userinfo(name, email, date, bloodgroup);
                databaseReference.setValue(userinfo);

                etName.setEnabled(false);
                etDOB.setEnabled(false);
                etBloodGroup.setEnabled(false);

                Toast.makeText(EditProfileActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            }
        }); //end of fabSave

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(Intent.ACTION_PICK);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                // startActivityForResult(intent, REQUEST_IMAGE_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_IMAGE_PICK);
            }
        });

    } //end of onCreate

    private void setProfilePic() {
        storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imageView_profile_pic);
                    }
                });
    } //end of setProfilePic()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  //data is the imageReturnedIntent

        if ((requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data.getData() != null)) {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            imagePath = data.getData();   //here uri is the selected image
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                imageView_profile_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //this.imageView_profile_pic.setImageURI(imagePath);

            //assign filepath for the image
            //final StorageReference filePath = mStorage.child("Photos").child(etName.getText().toString().trim());
            StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images")
                    .child("Profile Pic"); // UserID/Images/ProfilePic.ext
            UploadTask uploadTask = imageReference.putFile(imagePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Upload failed! Please check your internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                }
            });

        }   //end of ImagePick/Capture test

    } //end of onActivityResult

} //end of EditProfileActivity