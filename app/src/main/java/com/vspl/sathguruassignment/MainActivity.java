package com.vspl.sathguruassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String logedInEmail = sharedPreferences.getString("LogedInUser","");
        if(!logedInEmail.isEmpty()){
            Intent intent = new Intent(MainActivity.this,HomeScreen.class);
            intent.putExtra("email", logedInEmail);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this,LoginActiviy.class);
            startActivity(intent);
        }

    }
}