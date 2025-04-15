package com.example.modulus.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelperLogin extends SQLiteOpenHelper {
    private static final String dbName = "sutdModules.db";
    private static final String TABLE_NAME = "login";
    private static String dbPath = "/data/data/com.example.modulus/databases/";
    SQLiteDatabase db;
    private final Context mContext;
    private static final String COL_EMAIL = "Email";
    private static final String COL_PASSWORD = "Password";


    public DataBaseHelperLogin(Context context) {
        super(context, dbName, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)";

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
        boolean mDatabaseExists = checkDatabase();
        Log.d("DB", "create");

        if (!mDatabaseExists) {
            this.getReadableDatabase();
            this.close();
        }
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
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("DEBUG", "Database onOpen called.");
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while (cursor.moveToNext()) {
            Log.d("DBTables", "Table: " + cursor.getString(0));
        }
        cursor.close();
    }


    public boolean insertUser(String email,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL,email);
        values.put(COL_PASSWORD,password);

        long result = db.insert(TABLE_NAME,null,values);

        return result != -1;
    }

    public Boolean ifEmailExits(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }

    }

    public Boolean checkEmailPasswordValid(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{email,password});
        if(cursor.getCount() > 0) {
            Log.d("DBTables", "true");
            return true;

        }else {
            return false;
        }

    }




}