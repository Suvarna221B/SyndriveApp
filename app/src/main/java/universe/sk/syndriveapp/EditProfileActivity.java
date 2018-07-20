package universe.sk.syndriveapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etBloodGroup, etDOB;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FloatingActionButton fabEdit, fabSave, fabProfilePic;

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
        fabProfilePic = findViewById(R.id.fabProfilePic);

        etName.setEnabled(false);
        etDOB.setEnabled(false);
        etBloodGroup.setEnabled(false);
        etEmail.setEnabled(false);
        fabSave.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setEnabled(true);
                etDOB.setEnabled(true);
                etBloodGroup.setEnabled(true);
                fabSave.setEnabled(true);
            }
        });
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.setEnabled(false);
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String bloodgroup = etBloodGroup.getText().toString();
                String date = etDOB.getText().toString();

                Userinfo userinfo = new Userinfo(name, email, date, bloodgroup);
                databaseReference.setValue(userinfo);

                etName.setEnabled(false);
                etDOB.setEnabled(false);
                etBloodGroup.setEnabled(false);
            }
        });

    }
}
