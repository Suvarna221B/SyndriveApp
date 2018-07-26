package universe.sk.syndriveapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddContacts extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    Button registerbtn;
    EditText name1, num1, name2, num2, name3, num3;
    String emname1,emname2,emname3;
    int emnum1,emnum2,emnum3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.contacts);
        actionBar.setTitle(" Add Emergency Contacts");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = this.getSharedPreferences("universe.sk.syndriveapp.addcontacts", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        registerbtn= findViewById(R.id.registerbtn);

        name1 = findViewById(R.id.name1);
        num1 = findViewById(R.id.num1);
        name2 = findViewById(R.id.name2);
        num2 = findViewById(R.id.num2);
        name3 = findViewById(R.id.name3);
        num3 = findViewById(R.id.num3);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value=checkData();
                if(value==1) {
                    emname1 = name1.getText().toString();
                    emnum1 = Integer.parseInt(num1.getText().toString());
                    emname2 = name2.getText().toString();
                    emnum2 = Integer.parseInt(num2.getText().toString());
                    emname3 = name3.getText().toString();
                    emnum3 = Integer.parseInt(num3.getText().toString());

                /*
                if (emname1.isEmpty() || emname2.isEmpty() || emname3.isEmpty()){
                    startActivity(new Intent(AddContacts.this, RegistrationActivity.class));
                    Toast.makeText(AddContacts.this, "", Toast.LENGTH_SHORT).show();
                }
                */
                    editor.putString("name1", emname1);
                    editor.putInt("num1", emnum1);
                    editor.putString("name2", emname2);
                    editor.putInt("num2", emnum2);
                    editor.putString("name3", emname3);
                    editor.putInt("num3", emnum3);

                    editor.apply();
                    finish();
                    Toast.makeText(AddContacts.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddContacts.this, NavigationActivity.class));
                }
            }
        });
    }

    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

            int checkData(){
                if(isEmpty(name1) || isEmpty(name2) || isEmpty(name3) || isEmpty(num1) || isEmpty(num2) || isEmpty(num3)){
                    Toast.makeText(AddContacts.this,"Please fill all contact details",Toast.LENGTH_SHORT).show();
                    return -1;
                }
                else
                    return 1;
            }
        }







