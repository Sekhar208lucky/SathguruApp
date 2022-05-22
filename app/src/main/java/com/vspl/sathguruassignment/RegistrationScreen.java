package com.vspl.sathguruassignment;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vspl.sathguruassignment.database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CheckBox androidSDK, java, flutter, kotlin, swift, git, nodeJs;
    Button btnSignIn;
    EditText firstNameEditText, lastNameEditText, mobileEditText, emailEdiText, passwordEditText, confirmPasswordEditText;
    String[] genders = {"Select Gender", "Male", "Female", "Transgender"};
    String genderType = "";
    Spinner spinner;
    TextView tvDOB;
    final Calendar myCalendar = Calendar.getInstance();
    DatabaseHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        firstNameEditText = (EditText) findViewById(R.id.user_firstname);
        lastNameEditText = (EditText) findViewById(R.id.user_lastname);
        mobileEditText = (EditText) findViewById(R.id.user_mobileNo);
        emailEdiText = (EditText) findViewById(R.id.user_emailId);
        passwordEditText = (EditText) findViewById(R.id.user_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.user_confirm_password);
        androidSDK = (CheckBox) findViewById(R.id.chkAndroidSDK);
        flutter = (CheckBox) findViewById(R.id.chkFlutter);
        java = (CheckBox) findViewById(R.id.chkJava);
        kotlin = (CheckBox) findViewById(R.id.chkKotlin);
        swift = (CheckBox) findViewById(R.id.chkSwift);
        git = (CheckBox) findViewById(R.id.chkGit);
        nodeJs = (CheckBox) findViewById(R.id.chkNodeJs);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvDOB = (TextView) findViewById(R.id.user_dob);
        spinner = (Spinner) findViewById(R.id.gender_type);
        spinner.setOnItemSelectedListener(this);

        dbhandler = new DatabaseHandler(this);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegistrationScreen.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firstNameEditText.getText().toString().isEmpty()) {
                    if (!lastNameEditText.getText().toString().isEmpty()) {
                        String mblNo = mobileEditText.getText().toString();
                        if (!tvDOB.getText().toString().isEmpty()) {
                            if (!genderType.isEmpty() && !genderType.equalsIgnoreCase("Select gender")) {
                                if (android.util.Patterns.PHONE.matcher(mblNo).matches()) {
                                    String email = emailEdiText.getText().toString();
                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        if (!passwordEditText.getText().toString().isEmpty()) {
                                            if (!confirmPasswordEditText.getText().toString().isEmpty() &&
                                                    passwordEditText.getText().toString().equalsIgnoreCase(confirmPasswordEditText.getText().toString())) {

                                                if(!dbhandler.isUserExists(emailEdiText.getText().toString())){
                                                    ContentValues userData = new ContentValues();
                                                    userData.put("user_mail", emailEdiText.getText().toString());
                                                    userData.put("password", passwordEditText.getText().toString());
                                                    userData.put("firstname", firstNameEditText.getText().toString());
                                                    userData.put("lastname", lastNameEditText.getText().toString());
                                                    userData.put("dob", tvDOB.getText().toString());
                                                    userData.put("gender", genderType);
                                                    userData.put("skills", getSkills());
                                                    userData.put("mobile", mobileEditText.getText().toString());

                                                    registerUser(userData);
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "User already existed", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Enter valid EmailId ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter valid Mobile Number", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Selec gender type", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Date of birth", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter last name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter First name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser(ContentValues userData) {

        dbhandler.addUser(userData);
        Intent intent = new Intent(RegistrationScreen.this, LoginActiviy.class);
        startActivity(intent);
    }

    private String getSkills() {
        String result = "";
        if (flutter.isChecked()) {
            result += "Flutter";
        }
        if (kotlin.isChecked()) {
            result += "," + "Kotlin";
        }
        if (java.isChecked()) {
            result += "," + "Java";
        }
        if (androidSDK.isChecked()) {
            result += "," + "AndroidSDK";
        }
        if (swift.isChecked()) {
            result += "," + "Swift";
        }
        if (git.isChecked()) {
            result += "," + "GIT";
        }
        if (nodeJs.isChecked()) {
            result += "," + "NodeJs";
        }
        return result;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        tvDOB.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        genderType = genders[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Auto-generated method stub
    }

}