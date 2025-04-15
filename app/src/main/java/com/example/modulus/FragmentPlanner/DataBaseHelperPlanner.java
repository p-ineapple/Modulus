package com.example.modulus.FragmentPlanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.modulus.FragmentInsights.DataBaseHelperInsights;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataBaseHelperPlanner extends DataBaseHelperInsights {
    private static final String plannerTable = "planner";
    public DataBaseHelperPlanner(Context context) {
        super(context);
    }
    public ArrayList<PlannerModel> getPlanner(String pillar) {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ModuleModel> moduleList = this.getAllModules();
        ArrayList<PlannerModel> result = new ArrayList<PlannerModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(plannerTable, null, null, null, null, null, null);

        for (int i = 1; i <= 8; i++) {
            PlannerModel planner = new PlannerModel("Term " + i);
            result.add(planner);
        }
        while (c.moveToNext()) {
            int index = c.getInt(1);
            int colIndex = c.getColumnIndex(pillar);
            String id = c.getString(colIndex);
            if (id != null) {
                ModuleModel module = moduleList.stream().filter(m -> id.equals(m.getId())).findFirst().orElse(null);
                PlannerModel cPlanner = result.get(index - 1);
                ArrayList<ModuleModel> newMods = (ArrayList<ModuleModel>) cPlanner.getModules();
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
