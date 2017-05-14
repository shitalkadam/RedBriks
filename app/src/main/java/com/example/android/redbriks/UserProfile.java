package com.example.android.redbriks;

/**
 * Created by sesharika on 4/25/16.
 */
public class UserProfile {

    private String email;
    private String firstName;
    private String password;

    public UserProfile(String email, String firstName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
