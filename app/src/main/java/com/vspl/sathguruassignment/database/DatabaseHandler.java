package com.vspl.sathguruassignment.database;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sathguru_db";
    private static final String USERS_TABLE = "users";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addUser(ContentValues content) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insertOrThrow(USERS_TABLE, null, content);
            db.close();

        }catch (SQLiteConstraintException e)
        {
            Log.e("Duplicate Entry",""+e);
            db.close();
        }
        // Closing database connection
        //2nd argument is String containing nullColumnHack

    }

    public void updateUser(ContentValues content) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.update(USERS_TABLE, content, "user_mail=?",new String[] { content.getAsString("user_mail") });
//            db.insertOrThrow(USERS_TABLE, null, content);
            db.close();

        }catch (SQLiteConstraintException e)
        {
            Log.e("Duplicate Entry",""+e);
            db.close();
        }

    }


    public JSONObject getUserData(String emailId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+USERS_TABLE + " WHERE user_mail = '" + emailId + "'";
        Cursor cursor = db.rawQuery(query, null);

        //JSONArray resultSet = new JSONArray();
        JSONObject rowObject = new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();


            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {

                    try {

                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));

                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }

            }

            cursor.moveToNext();
        }

        return rowObject;

    }
    public boolean isUserExists(String emailId){
        boolean isExists = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM users where user_mail = '"+emailId +"'", null);
            if(c.getCount()>0)
            {
                isExists = true;
            }
            }catch(Exception e)
    {
        e.printStackTrace();
    }
        return isExists;
    }

    @SuppressLint("Range")
    public boolean isValidCredentials(String mailId, String password) {

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT password FROM "+USERS_TABLE + " WHERE user_mail = '" + mailId + "'";
            Cursor cursor = db.rawQuery(query, null);

            cursor.moveToFirst();
            String dbPassword = "";
            for(int i = 0; i < cursor.getCount(); i++){
                dbPassword = cursor.getString(cursor.getColumnIndex("password"));
            }

            return password.equals(dbPassword);
        }
        catch (Exception e)
        {
            return false;
        }



    }

}