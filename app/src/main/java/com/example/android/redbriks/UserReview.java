package com.example.android.redbriks;

/**
 * Created by sesharika on 4/24/16.
 */
public class UserReview {

    private String email;
    private String review;
    private float rating;

    public UserReview(String email, String review, float rating){

        this.email = email;
        this.review = review;
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
