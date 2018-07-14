package universe.sk.syndriveapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etName,etEmailsign,etPassign,etConfirmPassign;
    private Button btn_register;
    private TextView tvExist;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SIGN UP");
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    String user_name = etName.getText().toString().trim();
                    String user_email = etEmailsign.getText().toString().trim();
                    String user_password = etPassign.getText().toString().trim();

                    //store in database:to be done after filling the contacts
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                                Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });


                    startActivity(new Intent(RegistrationActivity.this,NavigationActivity.class));
                }
            }
        });

        tvExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }


    private void setupUIViews()
    {
        etName = findViewById(R.id.etName);
        etEmailsign = findViewById(R.id.etEmailsign);
        etPassign = findViewById(R.id.etPassign);
        btn_register = findViewById(R.id.btn_register);
        tvExist = findViewById(R.id.tvExist);
        etConfirmPassign = findViewById(R.id.etConfirmPassign);

    }

    private Boolean validate()
    {
        Boolean result = false;

        String name = etName.getText().toString();
        String userpassword = etPassign.getText().toString();
        String email = etEmailsign.getText().toString();
        String confirmpass = etConfirmPassign.getText().toString();

        if(name.isEmpty() || userpassword.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Please enter all the details!", Toast.LENGTH_SHORT).show();
        }
        else
        {   if(userpassword.equals(confirmpass))
                result = true;
            else
            Toast.makeText(this, "Confirm password doesn't match with your password!", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

}

