package com.example.android.redbriks;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.smartstreet.R;
import com.firebase.client.Firebase;

public class Registration extends AppCompatActivity {
    static final String SCAN_BARCODE = "com.google.zxing.client.android.SCAN";
    Context context = this;

    EditText firstNameText,passwordText,emailText;
    UserRegistrationDbHelper userRegistrationHelper;
    SQLiteDatabase sqLiteDatabase;

    String fullNameArray[];
    String fullName;
    String firstName;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Firebase.setAndroidContext(this);
       //creating the registration form
        createViews();
        firstNameText = (EditText) findViewById(R.id.firstName);
    }

    private void createViews() {
        emailText = (EditText) findViewById(R.id.email);
        //adding the text chage listener to validate text entry
        emailText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validEmailAddress(emailText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        passwordText = (EditText) findViewById(R.id.password);
        //adding the text chage listener to validate text entry
        passwordText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validPassword(passwordText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    //method is invoked when adding user information into database
    private void formSubmit() {

        //getting data form views
        fullName = firstNameText.getText().toString();
        fullNameArray = fullName.split("\\r?\\n");
        firstName = fullNameArray[0];
        password = passwordText.getText().toString();
        email = emailText.getText().toString();

        userRegistrationHelper = new UserRegistrationDbHelper(context);
        sqLiteDatabase = userRegistrationHelper.getWritableDatabase();
        userRegistrationHelper.addInformations(firstName,email,password, sqLiteDatabase);
        Toast.makeText(getBaseContext(), "Registered successfully!", Toast.LENGTH_LONG).show();
        userRegistrationHelper.close();
    }
    //validating the entered fields
    private boolean validationCkeck() {
        boolean reValue = true;
        if (!UserValidationHelper.validEmailAddress(emailText, true)) reValue = false;
        if (!UserValidationHelper.validPassword(passwordText, true)) reValue = false;
        return reValue;
    }

    //method will invoke when user submits form
    public void submit(View view){
        if (validationCkeck()) {
            formSubmit();
            Intent home_intent = new Intent(this, MainActivity.class);
            home_intent.putExtra("firstname", firstName);
            home_intent.putExtra("email",email);
            startActivity(home_intent);
            this.finish();
        }
        else
            Toast.makeText(Registration.this, "Form contains error", Toast.LENGTH_LONG).show();
    }
   //launches the home page
    public void home(View view){
        Intent home_intent = new Intent(this,MainActivity.class);
        startActivity(home_intent);
        this.finish();
    }
}
