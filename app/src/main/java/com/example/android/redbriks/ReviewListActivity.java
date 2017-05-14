package com.example.android.redbriks;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.android.smartstreet.R;

import java.util.ArrayList;

public class ReviewListActivity extends AppCompatActivity {

    private ReviewArrayAdapter mAdapter;
    private ListView listView;
    private String myUsername;

    CommentDbHelper commentDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor commentCursor;
    private ArrayList<UserReview> mUserReviews;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getIntent().getExtras();
//        myUsername = bundle.getString("username");

        setContentView(R.layout.review_list_view);
        listView = (ListView) findViewById(R.id.list_view);

        final ImageButton button = (ImageButton) findViewById(R.id.add);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });

        getComments();
        mAdapter = new ReviewArrayAdapter(this,mUserReviews);
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComments();
        mAdapter = new ReviewArrayAdapter(this,mUserReviews);
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public ArrayList getComments() {
        mUserReviews = new ArrayList<UserReview>();
        commentDbHelper = new CommentDbHelper(getApplicationContext());
        sqLiteDatabase = commentDbHelper.getReadableDatabase();
        commentCursor = commentDbHelper.getComments(sqLiteDatabase);
        //getting comment from database
        UserReview userReview;
        if (commentCursor.moveToFirst()) {
            do {
                //public UserReview(String email, String review, float rating){
                userReview = new UserReview(commentCursor.getString(2),
                        commentCursor.getString(0),
                        commentCursor.getFloat(1));
                mUserReviews.add(userReview);
            } while (commentCursor.moveToNext());
        }
        return mUserReviews;
    }

    private void addItem() {
        DialogFragment df = new DialogFragment() {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.dialog_add, container);
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

                final Button confirmButton = (Button) view.findViewById(R.id.add_dialog_ok);
                final Button cancelButton = (Button) view.findViewById(R.id.add_dialog_cancel);

                final EditText reviewQuoteEditText = (EditText) view.findViewById(R.id.review_id);
                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_id);
                final EditText emailText = (EditText) view.findViewById(R.id.email_id);

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reviewQuote = reviewQuoteEditText.getText().toString();
                        float rating = ratingBar.getRating();
                        String email = emailText.getText().toString();


                        UserReview currentComment = new UserReview(email, reviewQuote, rating);

                        commentDbHelper = new CommentDbHelper(getApplicationContext());
                        sqLiteDatabase = commentDbHelper.getWritableDatabase();
                        commentDbHelper.addInformations(reviewQuote, rating, email, sqLiteDatabase);

                        Toast.makeText(ReviewListActivity.this, "Comment is saved ", Toast.LENGTH_LONG).show();
                        commentDbHelper.close();
                        mUserReviews.clear();
                        getComments();
                        mAdapter = new ReviewArrayAdapter(getApplicationContext(),mUserReviews);
                        listView.setAdapter(mAdapter);
                        dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                return view;
            }
        };
        df.show(getFragmentManager(), "");
    }
}


