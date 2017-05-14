package com.example.android.redbriks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shital on 10/12/16.
 */
public class UserRegistrationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "USERINFORMATION.DB";
    private static final int DATABASE_VERSION = 1;

    //creating the user database query

    private static final String CREATE_QUERY =
            "CREATE TABLE " + UserInformationContract.UserInfo.TABLE_NAME + "(" +
                    UserInformationContract.UserInfo.FIRST_NAME + " TEXT," +
                    UserInformationContract.UserInfo.USER_EMAIL + " TEXT," +
                    UserInformationContract.UserInfo.USER_PASSWORD + " TEXT);";

    public UserRegistrationDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("OPERATING DATABASE", "User registration database created.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the user database
        db.execSQL(CREATE_QUERY);
        Log.e("OPERATING DATABASE", "User Table created....");

    }
    //adding the new user's information in database
    public void addInformations(String firstName,String email, String password, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserInformationContract.UserInfo.FIRST_NAME, firstName);
        contentValues.put(UserInformationContract.UserInfo.USER_EMAIL, email);
        contentValues.put(UserInformationContract.UserInfo.USER_PASSWORD, password);
        db.insert(UserInformationContract.UserInfo.TABLE_NAME, null, contentValues);
        Log.e("OPERATING DATABASE", "New row inserted...");
    }
    //searching the username for logging in
    public Cursor getUser(String user_name, SQLiteDatabase db) {
        String[] projections = {UserInformationContract.UserInfo.USER_EMAIL, UserInformationContract.UserInfo.USER_PASSWORD,UserInformationContract.UserInfo.FIRST_NAME};
        String selection = UserInformationContract.UserInfo.USER_EMAIL + " LIKE ?";
        String[] selection_args = {user_name};
        Cursor cursor = db.query(UserInformationContract.UserInfo.TABLE_NAME, projections, selection, selection_args, null, null, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
