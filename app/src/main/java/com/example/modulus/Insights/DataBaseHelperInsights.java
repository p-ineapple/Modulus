package com.example.modulus.Insights;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.modulus.Class.Module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseHelperInsights extends SQLiteOpenHelper {
    private static final String dbName = "sutdModules.db";
    private static final String tableName = "sutdmodules";
    private static String dbPath = "/data/data/com.example.modulus/databases/";
    SQLiteDatabase db;
    private final Context mContext;
    private static final String col1 = "Tags";
    private static final String col2 = "Term";
    private static final String col3 = "ID";
    private static final String col4 = "Name";
    private static final String col5 = "Professors";
    private static final String col6 = "Prerequisites";
    public DataBaseHelperInsights(Context context) {
        super(context, dbName, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    private boolean checkDatabase(){
        try{
            final String mPath = dbPath + dbName;
            Log.d("check", "check");
            final File file = new File(mPath);
            return file.exists();
        }catch(SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }
    private void copyDatabase() throws IOException {
        try{
            InputStream mInputStream = mContext.getAssets().open(dbName);
            Log.d("pls", "help");
            String outFileName = dbPath + dbName;
            OutputStream mOutputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[2048];
            int length;
            while((length = mInputStream.read(buffer)) > 0){
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createDatabase() throws IOException{
        boolean mDatabaseExists = checkDatabase();
        Log.d("create", "create");
        if(mDatabaseExists){
            this.getReadableDatabase();
            this.close();
            try{
                copyDatabase();
            }catch(IOException mIOException){
                mIOException.printStackTrace();
                throw new Error("Error copying Database");
            }finally{
                this.close();
            }
        }
    }
    @Override
    public synchronized void close(){
        if(db != null){
            db.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();
    }
    public ArrayList<Module> getAllModules(){
        try{
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Module> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while(c.moveToNext()){
            String id = c.getString(2);
            String name = c.getString(3);
            Module module = new Module(id, name);
            module.setTags(Arrays.asList(c.getString(0).split(",")));
            module.setTerm(Arrays.asList(c.getString(1).split(",")));
            module.setProf(Arrays.asList(c.getString(4).split(",")));
            module.setPrerequisites(Arrays.asList(c.getString(5).split(",")));
            result.add(module);
        }
        c.close();
        db.close();

        return result;
    }
}