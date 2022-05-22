package com.vspl.sathguruassignment.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class Models extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sathguru_db";
    SQLiteDatabase db = this.getWritableDatabase();

    public Models(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        userModel();

    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void userModel()
    {
        String createsersTABLE = "\n" +
                "CREATE TABLE IF NOT EXISTS 'users' (\n" +
                "  'user_mail' text NOT NULL PRIMARY KEY,\n" +
                "  'password' varchar(40) DEFAULT NULL,\n" +
                "  'firstname' text DEFAULT NULL,\n" +
                "  'lastname' varchar(11) DEFAULT NULL,\n" +
                "  'mobile' varchar(11) DEFAULT NULL,\n" +
                "  'gender' varchar(11) DEFAULT NULL,\n" +
                "  'dob' varchar(11) DEFAULT NULL,\n" +
                "  'skills' text DEFAULT NULL\n" +
                ")";
        db.execSQL(createsersTABLE);
    }
}