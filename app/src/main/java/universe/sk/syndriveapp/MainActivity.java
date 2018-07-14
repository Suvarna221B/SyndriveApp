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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etemail,etpassword;
    private Button btn;
    private TextView tvRegister,tvForgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("LOGIN");

        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        btn = findViewById(R.id.btn);
        tvRegister =findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,NavigationActivity.class));
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                if(email.isEmpty() || password.isEmpty() )
                {
                    Toast.makeText(MainActivity.this, "Please enter all the details!", Toast.LENGTH_SHORT).show();
                }
                else
                    valid(email,password);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });
    }

    private void valid(String email,String password)
    {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, NavigationActivity.class));

                }
                else
                    Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
