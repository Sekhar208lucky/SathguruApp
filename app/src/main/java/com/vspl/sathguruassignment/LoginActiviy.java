package com.vspl.sathguruassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vspl.sathguruassignment.database.DatabaseHandler;
import com.vspl.sathguruassignment.database.Models;

public class LoginActiviy extends AppCompatActivity {

    EditText etEmailId,etPassword;
    Button btnLogin,btnSignUp;
    DatabaseHandler dbhandler;
    RelativeLayout loginLayOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);
        loginLayOut = (RelativeLayout)findViewById(R.id.loginPageLayout);
        etEmailId = (EditText) findViewById(R.id.user_emailId);
        etPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.bntSignUp);

        new Models(this);
        dbhandler = new DatabaseHandler(this);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailId.getText().toString();
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!etPassword.getText().toString().isEmpty() &&
                            !etPassword.getText().toString().equalsIgnoreCase("")){
                        if(dbhandler.isUserExists(email)){
                            if(dbhandler.isValidCredentials(email, etPassword.getText().toString())){
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("LogedInUser",email);
                                myEdit.commit();
                                Intent intent = new Intent(LoginActiviy.this,HomeScreen.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActiviy.this, "Invalid Password", Toast.LENGTH_LONG).show();
                                // wrong password
                            }
                        }else{
                            Toast.makeText(LoginActiviy.this, "User not exists", Toast.LENGTH_LONG).show();
                            // not exists
                        }


                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter correct password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter valid EmailId", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActiviy.this,RegistrationScreen.class);
                startActivity(intent);
            }
        });
    }
}