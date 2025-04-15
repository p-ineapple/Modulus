package com.example.modulus.FragmentHome;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.modulus.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperHome extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MODULUS";
    private static final String TABLE_NAME = "HOME_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static final String COL_4 = "DATE";
    private static final String COL_5 = "TIME";

    private static final String COL_6 = "CATEGORY";

    private final String TAG = "Home DB";
    public DataBaseHelperHome(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER, DATE DATE, TIME TEXT, CATEGORY TEXT)");
        Log.d(TAG, "Create to do database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDoModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, model.getTask());
        contentValues.put(COL_3, 0);
        contentValues.put(COL_4, model.getDate());
        contentValues.put(COL_5, model.getTime());
        contentValues.put(COL_6, model.getCategory());


        db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "Insert task");
    }

    public void updateTask(int id, String task, String date, String category,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, time);
        contentValues.put(COL_6, category);
        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Update task");
    }

    public void updateStatus(int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, status);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }
    public void updateDate(int id, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, date);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateTime(int id, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, time);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateCategory(int id, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6, category);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Task deleted");
    }
    @SuppressLint("Range")
    public List<ToDoModel> getDateTask(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try{
            String selection = COL_4 + " = ?";
            String[] selectionArgs = { date };

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel toDoModel = new ToDoModel();
                        toDoModel.setId((cursor.getInt(cursor.getColumnIndex(COL_1))));
                        toDoModel.setTask((cursor.getString(cursor.getColumnIndex(COL_2))));
                        toDoModel.setStatus((cursor.getInt(cursor.getColumnIndex(COL_3))));
                        toDoModel.setDate((cursor.getString(cursor.getColumnIndex(COL_4))));
                        toDoModel.setTime((cursor.getString(cursor.getColumnIndex(COL_5))));
                        toDoModel.setCategory((cursor.getString(cursor.getColumnIndex(COL_6))));
                        modelList.add(toDoModel);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        Log.d(TAG, "Get all tasks");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_NAME,null,null,null,null,null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel toDoModel = new ToDoModel();
                        toDoModel.setId((cursor.getInt(cursor.getColumnIndex(COL_1))));
                        toDoModel.setTask((cursor.getString(cursor.getColumnIndex(COL_2))));
                        toDoModel.setStatus((cursor.getInt(cursor.getColumnIndex(COL_3))));
                        toDoModel.setDate((cursor.getString(cursor.getColumnIndex(COL_4))));
                        toDoModel.setTime((cursor.getString(cursor.getColumnIndex(COL_5))));
                        toDoModel.setCategory((cursor.getString(cursor.getColumnIndex(COL_6))));
                        modelList.add(toDoModel);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        Log.d(TAG, "Retrieved all tasks");
        return modelList;
    }

    @SuppressLint("Range")
    public List<ToDoModel> getStatustask(int status, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        try {
            // Combined WHERE clause for both status and date
            String selection = COL_3 + " = ? AND " + COL_4 + " = ?";
            String[] selectionArgs = {String.valueOf(status), date};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoModel toDoModel = new ToDoModel();
                    toDoModel.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                    toDoModel.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                    toDoModel.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                    toDoModel.setDate(cursor.getString(cursor.getColumnIndex(COL_4)));
                    toDoModel.setTime(cursor.getString(cursor.getColumnIndex(COL_5)));
                    toDoModel.setCategory(cursor.getString(cursor.getColumnIndex(COL_6)));
                    modelList.add(toDoModel);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return modelList;
    }


}