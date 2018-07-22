package universe.sk.syndriveapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etBloodGroup, etDOB;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FloatingActionButton fabEdit, fabSave, fabCamera, fabGallery;
    CircleImageView imageView_profile_pic;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    public Task<Uri> downloadUri;

    public static final int REQUEST_IMAGE_PICK = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 0;

    private static final int REQUEST_CAMERA = 1000;

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
        fabCamera = findViewById(R.id.fabCamera);
        fabGallery = findViewById(R.id.fabGallery);
        imageView_profile_pic = findViewById(R.id.imageView_profile_pic);

        etName.setEnabled(false);
        etDOB.setEnabled(false);
        etBloodGroup.setEnabled(false);
        etEmail.setEnabled(false);
        fabSave.setEnabled(false);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);         //upload progress dialog

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

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

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                }

                /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } */
            }
        });

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

    } //end of onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  //data is the imageReturnedIntent

        if ((requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) || (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)) {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();   //here uri is the selected image
            this.imageView_profile_pic.setImageURI(uri);
            //assign filepath for the image
            final StorageReference filePath = mStorage.child("Photos").child(etName.getText().toString().trim());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    downloadUri = filePath.getDownloadUrl();
                    /* Glide.with(EditProfileActivity.this)
                            .load(filePath)
                            .into(imageView_profile_pic); */
                    /* Picasso.get()
                            .load(downloadUri)
                            .resize(150, 150)
                            .centerCrop()
                            .into(imageView_profile_pic); */
                    Toast.makeText(EditProfileActivity.this, "Image upload successful!", Toast.LENGTH_SHORT).show();

                }
            });
        }   //end of ImagePick/Capture test

    } //end of onActivityResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.CAMERA)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("This permission is required in order to capture photos. Please permit it")
                        .setTitle("Permission required!");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);
                    }
                });

                dialog.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditProfileActivity.this, "Cannot capture photos!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        }
    } //end of onRequestPermissionsResult

} //end of EditProfileActivity