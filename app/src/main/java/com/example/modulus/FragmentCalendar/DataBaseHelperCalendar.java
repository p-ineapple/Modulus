package com.example.modulus.FragmentCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.modulus.R;
import com.example.modulus.Utils.DataBaseHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@SuppressLint({"Range", "SdCardPath"})

public class DataBaseHelperCalendar extends DataBaseHelper {
    private static final String TABLE_NAME = "timetable";
    private final String TAG = "DatabaseCalendar";

    //Column names
    private static final String COL_COLOR_ID = "colorId";
    private static final String COL_ID = "_id";
    private static final String COL_START_YEAR = "startTime_YEAR";
    private static final String COL_START_MONTH = "startTime_MONTH";
    private static final String COL_START_DAY = "startTime_DAY_OF_MONTH";
    private static final String COL_START_HOUR = "startTime_HOUR_OF_DAY";
    private static final String COL_START_MINUTE = "startTime_MINUTE";
    private static final String COL_END_HOUR = "endTime_HOUR_OF_DAY";
    private static final String COL_END_MINUTE = "endTime_MINUTE";
    private static final String COL_NAME = "name";
    private static final String COL_LOCATION = "location";

    //Constructor
    public DataBaseHelperCalendar(Context context) {
        super(context);
    }

    //Call when database is first created
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
        Log.i(TAG, "Table " + TABLE_NAME + " created");
    }

    //Call when database needs to upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    //Retrieve all Popup objects from database
    public List<Popup> getAllPop(){
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get readable instance of database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        List<Popup> popups = new ArrayList<>();

        db.beginTransaction();
        try{
            //Query all rows from timetable table
            c = db.query(TABLE_NAME,null,null,null,null,null, null);
            if(c != null){
                int idcount = 1;
                if(c.moveToFirst()){
                    do{
                        //Get colourID and assign to android colour resource
                        int colorId = c.getInt(c.getColumnIndex(COL_COLOR_ID));
                        int colorR = getColourR(colorId);

                        //Create calendar for event's date
                        Calendar eventDate = Calendar.getInstance();
                        int startYear = c.getInt(c.getColumnIndex(COL_START_YEAR));
                        int startMonth = c.getInt(c.getColumnIndex(COL_START_MONTH));
                        int startDay = c.getInt(c.getColumnIndex(COL_START_DAY));
                        eventDate.set(startYear,startMonth,startDay);

                        //Clone eventDate to set startTime
                        Calendar startTime = (Calendar)eventDate.clone();
                        int startHour = c.getInt(c.getColumnIndex(COL_START_HOUR));
                        int startMinute = c.getInt(c.getColumnIndex(COL_START_MINUTE));
                        startTime.set(Calendar.HOUR_OF_DAY,startHour);
                        startTime.set(Calendar.MINUTE,startMinute);

                        //Clone eventDate to set endTime
                        Calendar endTime = (Calendar)eventDate.clone();
                        int endHour = c.getInt(c.getColumnIndex(COL_END_HOUR));
                        int endMinute = c.getInt(c.getColumnIndex(COL_END_MINUTE));
                        endTime.set(Calendar.HOUR_OF_DAY,endHour);
                        endTime.set(Calendar.MINUTE,endMinute);

                        //Retrieve class name and time
                        String name = c.getString(c.getColumnIndex(COL_NAME));
                        String location = c.getString(c.getColumnIndex(COL_LOCATION));

                        //Create new Popup object and add to list
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

    //Maps Database colorId to actual Android colour resource
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
}



