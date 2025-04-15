package com.example.modulus.FragmentInsights;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.R;

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
    private static final String plannerTable = "planner";
    private static String dbPath = "/data/data/com.example.modulus/databases/";
    SQLiteDatabase db;
    private final Context mContext;
    private static final String col0 = "Pillar";
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

    public ArrayList<ModuleModel> getAllModules() {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ModuleModel> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while (c.moveToNext()) {
            String id = c.getString(3);
            String name = c.getString(4);
            ModuleModel module = new ModuleModel(id, name);
            module.setPillar(c.getString(0));
            module.setTags(Arrays.asList(c.getString(1).split(",")));
            module.setTerm(Arrays.asList(c.getString(2).split(",")));
            module.setProf(Arrays.asList(c.getString(5).split(",")));
            module.setPrerequisites(Arrays.asList(c.getString(6).split(",")));
            module.setDescription(c.getString(8));
            module.setColor(getColourR(c.getString(0)));
            result.add(module);
        }
        c.close();
        db.close();

        return result;
    }

    private int getColourR(String Pillar){
        switch (Pillar){
            case "ASD":
                return R.color.ASD;
            case "EPD":
                return R.color.EPD;
            case "ESD":
                return R.color.ESD;
            case "DAI":
                return R.color.DAI;
            case "ISTD":
                return R.color.ISTD;
            case "HASS":
                return R.color.HASS;
            case "SMT":
                return R.color.SMT;
            default:
                return R.color.OTHERS;
        }
    }
}