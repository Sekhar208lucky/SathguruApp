package com.vspl.sathguruassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vspl.sathguruassignment.database.DatabaseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeScreen extends AppCompatActivity {

    DatabaseHandler dbhandler;
    TextView name,userAge,gender,skills,emailId,mobileNo,password;
    Button btnUpdate,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_screen);

        name = (TextView)findViewById(R.id.tvName);
        userAge = (TextView)findViewById(R.id.tvage);
        gender = (TextView)findViewById(R.id.tvgender);
        skills = (TextView)findViewById(R.id.skills);
        emailId = (TextView)findViewById(R.id.tvemail);
        mobileNo = (TextView)findViewById(R.id.tvmobile);
        password = (TextView)findViewById(R.id.tvpassword);
        btnUpdate= (Button)findViewById(R.id.btnUpdate);
        btnLogout= (Button)findViewById(R.id.btnLogout);


        dbhandler = new DatabaseHandler(this);
        String mailId = getIntent().getStringExtra("email");
        JSONObject jsonObjectUserData = dbhandler.getUserData(mailId);


        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            int age=0;
            try {
                Date date = format.parse(jsonObjectUserData.getString("dob"));
                System.out.println(date);
                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                dob.setTime(date);
                int year = dob.get(Calendar.YEAR);
                int month = dob.get(Calendar.MONTH);
                int day = dob.get(Calendar.DAY_OF_MONTH);

                dob.set(year, month+1, day);

                  age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    age--;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            name.setText("Full Name : "+jsonObjectUserData.getString("firstname")+" "+jsonObjectUserData.getString("lastname"));
            userAge.setText("Age : "+String.valueOf(age));
            gender.setText("Gender : "+jsonObjectUserData.getString("gender"));
            skills.setText("Skills : "+jsonObjectUserData.getString("skills"));
            emailId.setText("EmailId : "+jsonObjectUserData.getString("user_mail"));
            mobileNo.setText("Mobile Number : "+jsonObjectUserData.getString("mobile"));
            password.setText("Password : "+jsonObjectUserData.getString("password"));
        } catch (Exception e) {
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, UpdateScreen.class);
                intent.putExtra("email",mailId);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.remove("LogedInUser");
                myEdit.commit();
                Intent intent = new Intent(HomeScreen.this, LoginActiviy.class);
                startActivity(intent);

            }
        });

    }
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}