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
public class CommentDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "COMMENTS.DB";

    private static final String CREATE_QUERY =
            "CREATE TABLE " + UserCommentsContract.Comments.TABLE_NAME + "(" +
                    UserCommentsContract.Comments.COMMENT + " TEXT," +
                    UserCommentsContract.Comments.RATING + " TEXT," +
                    UserCommentsContract.Comments.EMAIL + " TEXT);";

    public CommentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("COMMENT DATABASE", "Database created / opened....");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating/opening database
        db.execSQL(CREATE_QUERY);
        Log.e("COMMENT DATABASE", "Table created....");
    }

    //this method will add new comment into database
    public void addInformations(String comment,Float rating,String email, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserCommentsContract.Comments.COMMENT, comment);
        contentValues.put(UserCommentsContract.Comments.RATING, rating);
        contentValues.put(UserCommentsContract.Comments.EMAIL, email);
        db.insert(UserCommentsContract.Comments.TABLE_NAME, null, contentValues);
        Log.e("COMMENT DATABASE", "New row inserted...");
    }

    //fetching all the comments from database
    public Cursor getComments(SQLiteDatabase db) {
        Cursor commentCursor;
        String[] commentProjection = {UserCommentsContract.Comments.COMMENT,UserCommentsContract.Comments.RATING,UserCommentsContract.Comments.EMAIL};
        commentCursor = db.query(UserCommentsContract.Comments.TABLE_NAME, commentProjection, null, null, null, null, null);
        return commentCursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
