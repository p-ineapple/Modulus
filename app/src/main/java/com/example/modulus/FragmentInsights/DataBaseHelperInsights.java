package com.example.modulus.FragmentInsights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.modulus.Utils.ModuleGraph;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;
import com.example.modulus.Utils.DataBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelperInsights extends DataBaseHelper {
    private static final String tableName = "sutdmodules";
    private final String TAG = "DatabaseInsights";

    private static final String COL_0 = "Pillar";
    private static final String COL_1 = "Tags";
    private static final String COL_2 = "Term";
    private static final String COL_3 = "ID";
    private static final String COL_4 = "Name";
    private static final String COL_5 = "Professors";
    private static final String COL_6 = "Prerequisites";
    private static final String COL_7 = "Cost";
    private static final String COL_8 = "Description";
    private static final String COL_9 = "Type";


    public DataBaseHelperInsights(Context context) {
        super(context);
    }

    @SuppressLint("Range")
    public ArrayList<ModuleModel> getAllModules() {
        try {
            createDatabase();
            Log.d(TAG, "DB created");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "DB error");
        }
        ArrayList<ModuleModel> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(COL_3));
            String name = c.getString(c.getColumnIndex(COL_4));
            ModuleModel module = new ModuleModel(id, name);
            String pillar = c.getString(c.getColumnIndex(COL_0));
            module.setPillar(pillar);
            module.setTags(Arrays.asList(c.getString(c.getColumnIndex(COL_1)).split(",")));
            module.setTerm(Arrays.asList(c.getString(c.getColumnIndex(COL_2)).split(",")));
            module.setProf(Arrays.asList(c.getString(c.getColumnIndex(COL_5)).split(",")));
            module.setPrerequisites(Arrays.asList(c.getString(c.getColumnIndex(COL_6)).split(",")));
            module.setDescription(c.getString(c.getColumnIndex(COL_8)));

            module.setColor(getColourR(pillar));
            module.setImage(getImage(this.getContext(),pillar));
            module.setType(c.getString(c.getColumnIndex(COL_9)));

            result.add(module);
        }
        Log.d(TAG, result.size() + " modules added");
        c.close();
        db.close();

        return result;
    }

    @SuppressLint("Range")
    public ModuleGraph<String> getGraph(){
        try {
            createDatabase();
            Log.d(TAG, "DB created");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "DB error");
        }
        ModuleGraph<String> g = new ModuleGraph<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(COL_3));
            List<String> preReq = Arrays.asList(c.getString(c.getColumnIndex(COL_6)).split(","));
            String cost = c.getString(c.getColumnIndex(COL_7));
            if(cost.equals("NIL")){
                g.addVertex(id, cost);
            }else if(cost.equals("Soft")){
                for(String s: preReq){
                    if(s.contains("/")){
                        List<String> softPreReq = Arrays.asList(s.split("/"));
                        for(String s1: softPreReq){
                            g.addEdge(id, s, cost);
                        }
                    }else{
                        g.addEdge(id, s, cost);
                    }

                }
            }else{
                for(String s: preReq){
                    g.addEdge(id, s, cost); // Destination vertex may contain "/", ignore for now
                }
            }
        }
        Log.d(TAG, g.getVertexCount() + " vertices added");
        c.close();
        db.close();

        return g;
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



    private Drawable getImage(Context context, String Pillar){
        int resId;
        switch (Pillar){
            case "ASD":
                resId = R.drawable.asd;
                break;
            case "EPD":
                resId = R.drawable.epd;
                break;
            case "ESD":
                resId = R.drawable.esd;
                break;
            case "DAI":
                resId = R.drawable.dai;
                break;
            case "ISTD":
                resId = R.drawable.istd;
                break;
            case "HASS":
                resId = R.drawable.hass;
                break;
            case "SMT":
                resId = R.drawable.smt;
                break;
            default:
                resId = R.drawable.others;
                break;
        }
        return ContextCompat.getDrawable(context, resId);
    }

}