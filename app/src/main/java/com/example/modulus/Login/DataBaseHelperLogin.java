package com.example.modulus.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressLint({"Range", "SdCardPath", "Recycle"})

public class DataBaseHelperLogin extends SQLiteOpenHelper {
    private static final String dbName = "sutdModules.db";
    private static final String TABLE_NAME = "login";
    private static final String dbPath = "/data/data/com.example.modulus/databases/";

    private SQLiteDatabase db;
    private final Context mContext;

    //Column names
    private static final String COL_EMAIL = "Email";
    private static final String COL_PASSWORD = "Password";

    private final String TAG = "DatabaseLogin";


    //Constructor
    public DataBaseHelperLogin(Context context) {
        super(context, dbName, null, 1);
        this.mContext = context;
    }

    //Call when database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("database login", "Create Database");
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)";

        db.execSQL(createTableStatement);
        Log.i(TAG, "Table " + TABLE_NAME + " created");

    }

    //Call when database needs to upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    //Check if database file exists
    private boolean checkDatabase() {
        try {
            final String mPath = dbPath + dbName;
            Log.i(TAG, "check");
            final File file = new File(mPath);
            return file.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Copies database to device local storage
    private void copyDatabase() throws IOException {
        boolean mDatabaseExists = checkDatabase();
        Log.i(TAG, "create");

        if (!mDatabaseExists) {
            this.getReadableDatabase();
            this.close();
        }
        try {
            InputStream mInputStream = mContext.getAssets().open(dbName);
            Log.i(TAG, "Copying Database");
            String outFileName = dbPath + dbName;
            OutputStream mOutputStream = Files.newOutputStream(Paths.get(outFileName));

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

    //Create database
    public void createDatabase() throws IOException {
        boolean mDatabaseExists = checkDatabase();
        Log.i(TAG, "create");
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

    //Log all table names when Database is opened
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "Database onOpen called.");
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while (cursor.moveToNext()) {
            Log.d("DBTables", "Table: " + cursor.getString(0));
        }
        cursor.close();
    }


    //Check if email and password pair exists
    public Boolean checkEmailPasswordValid(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{email,password});
        if(cursor.getCount() > 0) {
            Log.i("DBTables", "true");
            return true;
        }else {
            return false;
        }
    }
}