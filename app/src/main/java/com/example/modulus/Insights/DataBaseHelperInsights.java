package com.example.modulus.Insights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.modulus.Class.Module;
import com.example.modulus.Class.Planner;
import com.example.modulus.Class.ToDoModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DataBaseHelperInsights extends SQLiteOpenHelper {
    private static final String dbName = "sutdModules.db";
    private static final String tableName = "sutdmodules";
    private static final String plannerTable = "planner";
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

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public ArrayList<Module> getAllModules() {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Module> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while (c.moveToNext()) {
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

    public ArrayList<Planner> getPlanner() {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Module> moduleList = this.getAllModules();
        ArrayList<Planner> result = new ArrayList<Planner>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(plannerTable, null, null, null, null, null, null);

        for (int i = 1; i <= 8; i++) {
            Planner planner = new Planner("Term " + i);
            result.add(planner);
        }
        while (c.moveToNext()) {
            int index = c.getInt(1);
            String name = c.getString(3);
            if (Objects.equals(name, "Capstone")) {
                Module module = new Module(" ", "Capstone");
                Planner cPlanner = result.get(index - 1);
                ArrayList<Module> newMods = (ArrayList<Module>) cPlanner.getModules();
                newMods.add(module);
                cPlanner.setModules(newMods);
                result.set(index - 1, cPlanner);
            } else if (name != null) {
                Module module = moduleList.stream().filter(m -> name.equals(m.getName())).findFirst().orElse(null);
                Planner cPlanner = result.get(index - 1);
                ArrayList<Module> newMods = (ArrayList<Module>) cPlanner.getModules();
                newMods.add(module);
                cPlanner.setModules(newMods);
                result.set(index - 1, cPlanner);
            }
        }
        c.close();
        db.close();

        return result;
    }
}