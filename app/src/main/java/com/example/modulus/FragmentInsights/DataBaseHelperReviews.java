package com.example.modulus.FragmentInsights;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.modulus.Model.ReviewModel;
import com.example.modulus.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperReviews extends SQLiteOpenHelper {
    private static final String dbName = "MODULUSREVIEWS";
    private static final String tableName = "REVIEWS";
    private static final String col1 = "ID";
    private static final String col2 = "MODULEID";
    private static final String col3 = "USERNAME";
    private static final String col4 = "RATING";
    private static final String col5 = "REVIEW";
    ;
    private final String TAG = "Reviews DB";

    public DataBaseHelperReviews(Context context) {

        super(context, dbName, null, 2);
        Log.d(TAG, "Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, MODULEID TEXT, USERNAME TEXT, RATING TEXT, REVIEW TEXT)");
        Log.d(TAG, "Create review database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            // For databases at version 1 upgrading to version 2:
            if (oldVersion < 2) {
                // Example: Create a new USERS table without touching the existing REVIEWS table.
                String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS USERS (" +
                        "USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "USERNAME TEXT UNIQUE, " +
                        "EMAIL TEXT" +
                        ")";
                db.execSQL(createUsersTableSQL);
                Log.d(TAG, "USERS table created in onUpgrade (v2).");
            }

            // If you need to handle additional upgrades in future versions,
            // add more conditional blocks like:
            // if (oldVersion < 3) {
            //    // Further modifications/upgrades for version 3
            // }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void insertTask(ReviewModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, model.getModuleId());
        contentValues.put(col3, model.getUsername());
        contentValues.put(col4, model.getRating());
        contentValues.put(col5, model.getComment());


        db.insert(tableName, null, contentValues);
        Log.d(TAG, "Insert review");
    }

    @SuppressLint("Range")
    public List<ReviewModel> getModuleReviews(String moduleID){
        Log.d(TAG, "Get all module reviews");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try{
            String selection = col2 + " = ?";
            String[] selectionArgs = { moduleID };
            cursor = db.query(tableName,null,selection,selectionArgs,null,null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setId((cursor.getInt(cursor.getColumnIndex(col1))));
                        reviewModel.setModuleId((cursor.getString(cursor.getColumnIndex(col2))));
                        reviewModel.setUsername((cursor.getString(cursor.getColumnIndex(col3))));
                        reviewModel.setRating((cursor.getString(cursor.getColumnIndex(col4))));
                        reviewModel.setComment((cursor.getString(cursor.getColumnIndex(col5))));
                        modelList.add(reviewModel);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        Log.d(TAG, "Retrieved all reviews");
        return modelList;
    }

    public float getOverallRating(String moduleID) {
        float overallRating = 0f;
        SQLiteDatabase db = this.getReadableDatabase();
        // Query: calculate the average rating by casting the TEXT rating to REAL.
        String query = "SELECT AVG(CAST(" + col4 + " AS REAL)) AS avg_rating FROM "
                + tableName + " WHERE " + col2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{ moduleID });

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colIndex = cursor.getColumnIndex("avg_rating");
                if (colIndex == -1) {
                    Log.d(TAG, "avg_rating column not found!");
                } else if (cursor.isNull(colIndex)) {
                    Log.d(TAG, "avg_rating value is null for moduleID: " + moduleID);
                } else {
                    overallRating = (float) cursor.getDouble(colIndex);
                    Log.d(TAG, "Computed avg_rating: " + overallRating);
                }
            } else {
                Log.d(TAG, "No rows returned for moduleID: " + moduleID);
            }
            cursor.close();
        } else {
            Log.d(TAG, "Cursor is null for moduleID: " + moduleID);
        }

        return overallRating;
    }



}