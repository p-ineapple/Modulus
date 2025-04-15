package com.example.modulus.FragmentPlanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.modulus.FragmentInsights.DataBaseHelperInsights;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.TrackModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelperTracks extends DataBaseHelperInsights {
    private final ArrayList<ModuleModel> moduleList = this.getAllModules();
    private static final String tracksTable = "tracks";
    private final String TAG = "DB Tracks";
    public DataBaseHelperTracks(Context context) {
        super(context);
    }
    public String[] getTracks(String pillar) {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!pillar.equals("CSD") && !pillar.equals("EPD") && !pillar.equals("ESD")) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tracksTable, null, null, null, null, null, null);

        while (c.moveToNext()) {
            int colIndex = c.getColumnIndex(pillar);
            String track = c.getString(colIndex);
            if(track != null){
                result.add(track);
            }
        }
        result.add("No Specialization");
        c.close();
        db.close();

        return result.toArray(new String[0]);
    }

    public TrackModel getTrackModel(String track, String pillar) {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = this.getReadableDatabase();
        TrackModel trackModel = new TrackModel(track, pillar);
        List<ModuleModel> core = new ArrayList<>();
        List<ModuleModel> recMods = new ArrayList<>();
        List<ModuleModel> electives = new ArrayList<>();

        Cursor c = db.query(tracksTable, null, null, null, null, null, null);

        while (c.moveToNext()) {
            int colIndex = c.getColumnIndex(track);
            String data = c.getString(colIndex);
            if(data != null && data.contains(",")){
                String[] typeId = data.split(",");
                ModuleModel module = moduleList.stream().filter(m -> typeId[1].equals(m.getId())).findFirst().orElse(null);
                if(typeId[0].equals("Core")){
                    core.add(module);
                }else if(typeId[0].equals("Rec")){
                    recMods.add(module);
                }else{
                    electives.add(module);
                }
            }
        }
        trackModel.setCore(core);
        trackModel.setRecMods(recMods);
        trackModel.setElectives(electives);
        c.close();
        db.close();

        return trackModel;
    }

    public String[] getMinors() {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tracksTable, null, null, null, null, null, null);

        while (c.moveToNext()) {
            String minor = c.getString(0);
            if(minor != null){
                result.add(minor);
            }
        }

        c.close();
        db.close();

        return result.toArray(new String[0]);
    }

    public ArrayList<String[]> getMinorsEligibility() {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String[]> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tracksTable, null, null, null, null, null, null);

        while (c.moveToNext()) {
            String eligibility = c.getString(1);
            if(eligibility != null){
                String[] eligibilityList = eligibility.split(",");
                result.add(eligibilityList);
            }
        }
        c.close();
        db.close();

        return result;
    }
}
