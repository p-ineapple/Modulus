package com.example.modulus.FragmentCalendar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.modulus.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataBaseHelperCalendar extends SQLiteOpenHelper {
    private static final String dbName = "sutdModules.db";
    private static final String TABLE_NAME = "timetable";
    private static String dbPath = "/data/data/com.example.modulus/databases/";
    private SQLiteDatabase db;
    private final Context mContext;
    private static final String COL_COLOR_ID = "colorId";
    private static final String COL_ID = "_id";
    private static final String COL_START_YEAR = "startTime_YEAR";
    private static final String COL_START_MONTH = "startTime_MONTH";
    private static final String COL_START_DAY = "startTime_DAY_OF_MONTH";
    private static final String COL_START_HOUR = "startTime_HOUR_OF_DAY";
    private static final String COL_START_MINUTE = "startTime_MINUTE";
    private static final String COL_END_YEAR = "endTime_YEAR";
    private static final String COL_END_MONTH = "endTime_MONTH";
    private static final String COL_END_DAY = "endTime_DAY_OF_MONTH";
    private static final String COL_END_HOUR = "endTime_HOUR_OF_DAY";
    private static final String COL_END_MINUTE = "endTime_MINUTE";
    private static final String COL_NAME = "name";
    private static final String COL_LOCATION = "location";


    public DataBaseHelperCalendar(Context context) {
        super(context, dbName, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_START_YEAR + " INTEGER, " +
                COL_START_MONTH + " INTEGER, " +
                COL_START_DAY + " INTEGER, " +
                COL_START_HOUR + " INTEGER, " +
                COL_START_MINUTE + " INTEGER, " +
                COL_END_HOUR + " INTEGER, " +
                COL_END_MINUTE + " INTEGER, " +
                COL_COLOR_ID + " INTEGER)";
        db.execSQL(createTableStatement);
        Log.d("Database", "Table " + TABLE_NAME + " created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    private boolean checkDatabase() {
        try {
            final String mPath = dbPath + dbName;
            Log.d("check", "check");
            final File file = new File(mPath);
            return file.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        try {
            InputStream mInputStream = mContext.getAssets().open(dbName);
            Log.d("DB", "Copying Database");
            String outFileName = dbPath + dbName;
            OutputStream mOutputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[2048];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() throws IOException {
        boolean mDatabaseExists = checkDatabase();
        Log.d("create", "create");
        if (mDatabaseExists) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying Database");
            } finally {
                this.close();
            }
        }
    }



    @SuppressLint("Range")
    public List<Event> getAllEvents(){
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        List<Event> events = new ArrayList<>();

        db.beginTransaction();
        try{
            c = db.query(TABLE_NAME,null,null,null,null,null, null);
            if(c != null){
                int idcount = 1;
                if(c.moveToFirst()){
                    do{

                        int colorId = c.getInt(c.getColumnIndex(COL_COLOR_ID));
                        int colorR = getColourR(colorId);

                        //Create calendar for specific date
                        Calendar eventDate = Calendar.getInstance();
                        int startYear = c.getInt(c.getColumnIndex(COL_START_YEAR));
                        int startMonth = c.getInt(c.getColumnIndex(COL_START_MONTH));
                        int startDay = c.getInt(c.getColumnIndex(COL_START_DAY));
                        eventDate.set(startYear,startMonth,startDay);


                        //Create calendar for start
                        Calendar startTime = (Calendar)eventDate.clone();
                        int startHour = c.getInt(c.getColumnIndex(COL_START_HOUR));
                        int startMinute = c.getInt(c.getColumnIndex(COL_START_MINUTE));
                        startTime.set(Calendar.HOUR_OF_DAY,startHour);
                        startTime.set(Calendar.MINUTE,startMinute);


                        //Create calendar for end
                        Calendar endTime = (Calendar)eventDate.clone();
                        int endHour = c.getInt(c.getColumnIndex(COL_END_HOUR));
                        int endMinute = c.getInt(c.getColumnIndex(COL_END_MINUTE));
                        endTime.set(Calendar.HOUR_OF_DAY,endHour);
                        endTime.set(Calendar.MINUTE,endMinute);

                        String name = c.getString(c.getColumnIndex(COL_NAME));
                        String location = c.getString(c.getColumnIndex(COL_LOCATION));

                        Event event = new Event(idcount++,eventDate,startTime,endTime,name,location,colorR);
                        events.add(event);

                    }while (c.moveToNext());
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            if (c!= null) {
                c.close();
            }
        }
        return events;
    }

    @Override
    public synchronized void close(){
        if (db != null) {
            db.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();

    }

    private int getColourR(int colorId){
        switch (colorId){
            case 2:
                return R.color.calendar_green;
            case 3:
                return R.color.calendar_purple;
            case 5:
                return R.color.calendar_yellow;
            case 11:
                return R.color.calendar_red;
            default:
                return R.color.calendar_blue;
        }
    }


    @SuppressLint("Range")
    public List<Popup> getAllPop(){
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        List<Popup> popups = new ArrayList<>();

        db.beginTransaction();
        try{
            c = db.query(TABLE_NAME,null,null,null,null,null, null);
            if(c != null){
                int idcount = 1;
                if(c.moveToFirst()){
                    do{

                        int colorId = c.getInt(c.getColumnIndex(COL_COLOR_ID));
                        int colorR = getColourR(colorId);

                        //Create calendar for specific date
                        Calendar eventDate = Calendar.getInstance();
                        int startYear = c.getInt(c.getColumnIndex(COL_START_YEAR));
                        int startMonth = c.getInt(c.getColumnIndex(COL_START_MONTH));
                        int startDay = c.getInt(c.getColumnIndex(COL_START_DAY));
                        eventDate.set(startYear,startMonth,startDay);


                        //Create calendar for start
                        Calendar startTime = (Calendar)eventDate.clone();
                        int startHour = c.getInt(c.getColumnIndex(COL_START_HOUR));
                        int startMinute = c.getInt(c.getColumnIndex(COL_START_MINUTE));
                        startTime.set(Calendar.HOUR_OF_DAY,startHour);
                        startTime.set(Calendar.MINUTE,startMinute);


                        //Create calendar for end
                        Calendar endTime = (Calendar)eventDate.clone();
                        int endHour = c.getInt(c.getColumnIndex(COL_END_HOUR));
                        int endMinute = c.getInt(c.getColumnIndex(COL_END_MINUTE));
                        endTime.set(Calendar.HOUR_OF_DAY,endHour);
                        endTime.set(Calendar.MINUTE,endMinute);

                        String name = c.getString(c.getColumnIndex(COL_NAME));
                        String location = c.getString(c.getColumnIndex(COL_LOCATION));

                        Popup popup = new Popup(idcount++,eventDate,startTime,endTime,name,location,colorR);
                        popups.add(popup);

                    }while (c.moveToNext());
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            if (c!= null) {
                c.close();
            }
        }
        return popups;
    }
}



