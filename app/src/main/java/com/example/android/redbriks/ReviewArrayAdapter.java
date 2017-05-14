package com.example.android.redbriks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.smartstreet.R;

import java.util.ArrayList;

/**
 * Created by sesharika on 4/24/16.
 */
public class ReviewArrayAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    private ArrayList mUserReviews = new ArrayList();

    public ReviewArrayAdapter(Context context, ArrayList mUserReviews) {
        this.mUserReviews = mUserReviews;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return mUserReviews.size();
    }

    public void removeItem(UserReview userReview) {
        //TODO: Remove data from Firebase
    }

    public void addItem(UserReview userReview) {

    }

    public void updateItem(UserReview userReview, String newUsername, String newReview, float rating) {
        //TODO: Push changes to Firebase
    }

    @Override
    public UserReview getItem(int position) {
        return (UserReview) mUserReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        UserReview currentReviewData = getItem(position);

        mViewHolder.mComment.setText(currentReviewData.getReview());
        mViewHolder.mEmail.setText(currentReviewData.getEmail());
        mViewHolder.mRating.setRating(currentReviewData.getRating());

        return convertView;
    }

    private class MyViewHolder {
        TextView mComment, mEmail;
        RatingBar mRating;

        public MyViewHolder(View item) {
            mComment = (TextView) item.findViewById(R.id.row_comment);
            mEmail = (TextView) item.findViewById(R.id.row_email);
            mRating = (RatingBar) item.findViewById(R.id.row_rate);
        }
    }
}

