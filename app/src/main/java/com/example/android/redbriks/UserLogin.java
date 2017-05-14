package com.example.android.redbriks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.android.smartstreet.R;
import com.firebase.client.Firebase;

public class UserLogin extends AppCompatActivity {
    ImageButton logoButton;

    private static final String URL = "https://smartstreetapp.firebaseio.com";
    Firebase ref;

    UserRegistrationDbHelper userRegistrationHelper;
    SQLiteDatabase sqLiteDatabase;

    String passDb;
    String firstNameDb="";
    String emailDb;

    EditText emailText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_user_login);
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
    }

    //method will invoke when user clicks on login button
    public void submit(View view){
        //getting user name from view
        String userName = emailText.getText().toString();
        //getting password from view
        String password = passwordText.getText().toString();
        //opening the database and getting contents
        userRegistrationHelper = new UserRegistrationDbHelper(getApplicationContext());
        sqLiteDatabase = userRegistrationHelper.getReadableDatabase();
        Cursor cursor = userRegistrationHelper.getUser(userName, sqLiteDatabase);
        //matching the username and password
        if(cursor.moveToFirst()){
            emailDb = cursor.getString(0);
            passDb = cursor.getString(1);
            firstNameDb = cursor.getString(2);

            if(password.equals(passDb)) {
                //if username and password matches launches the home page
                Intent home_intent = new Intent(this,MainActivity.class);
                //putting user's name with hame page intent
                home_intent.putExtra("firstname", firstNameDb);
                home_intent.putExtra("email",emailDb);
                startActivity(home_intent);
                this.finish();
            }
            else{
                passwordText.setError("wrong password");
            }
        }
        else {
            emailText.setError("wrong username");
        }
    }
    //launches the registration activity
    public void register(View view){
        Intent register_intent = new Intent(this,Registration.class);
        startActivity(register_intent);
        this.finish();
    }
    //launches the home activity
    public void home(View view){
        Intent home_intent = new Intent(this,MainActivity.class);
        startActivity(home_intent);
        this.finish();
    }

}
