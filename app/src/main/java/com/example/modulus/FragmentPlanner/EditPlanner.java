package com.example.modulus.FragmentPlanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Adapter.EditPlannerAdapter;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EditPlanner extends AppCompatActivity {
    List<PlannerModel> editPlannerList = PlannerFragment.plannerList;
    ImageView backButton;
    Button confirmButton;
    RecyclerView editRecyclerView;
    SharedPreferences mPreferences;
    private final String TAG = "Edit PlannerModel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_planner_activity);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult( Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Result from DataEntryActivity is obtained
                        // Get the data and insert it into datasource
                        Log.d("TAG", "Back to Edit");
                        Bundle b = result.getData().getExtras();
                        String newModules = b.getString(EditPlannerMenu.KEY_NAME);
                        String term = b.getString(EditPlannerMenu.KEY_PATH);
                        System.out.println(term);
                        String[] updatedModules = newModules.split("\n");
                        List<ModuleModel> newPlannerModules = new ArrayList<ModuleModel>();
                        for(String moduleString: updatedModules){
                            for(ModuleModel module: InsightsFragment.moduleList){
                                if(moduleString.contains(module.getId())){
                                    newPlannerModules.add(module);
                                }
                            }
                        }
//                        for(PlannerModel planner: editPlannerList){
//                            String t = planner.getTerm();
//                            if(t.equals(term)){
//                                if(t.contains("7") || t.contains("8")){
//                                    newPlannerModules.add(new ModuleModel("", "Capstone"));
//                                }
//                                planner.setModules(newPlannerModules);
//                            }
//                        }
                        for(PlannerModel planner: editPlannerList) {
                            System.out.println(planner.getModules().toString());
                        }
                        System.out.println(newPlannerModules.toString());
                        Log.d(TAG, "Planners Updated");
                    }
                }

        );
        EditPlannerAdapter.OnItemClickListener listener = new EditPlannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlannerModel planner) {
                Intent intent = new Intent(EditPlanner.this, EditPlannerMenu.class);
                intent.putExtra("id", planner.getTerm());
                launcher.launch(intent);
            }
        };
        EditPlannerAdapter termButtonsAdapter = new EditPlannerAdapter(PlannerFragment.plannerList, listener);
        editRecyclerView = findViewById(R.id.editRecyclerView);
        editRecyclerView.setAdapter(termButtonsAdapter);
        editRecyclerView.setLayoutManager(new GridLayoutManager(EditPlanner.this, 2));

        mPreferences = this.getSharedPreferences(PlannerFragment.PREF_FILE, Context.MODE_PRIVATE);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefsEditor = mPreferences.edit();
                Gson gson = new Gson();
                ArrayList<String> terms = new ArrayList<>();
                ArrayList<String> plannerModules = new ArrayList<>();
                for (PlannerModel planner: editPlannerList) {
                    terms.add(planner.getTerm());
                    if (planner.getModules() != null){
                        for(ModuleModel module: planner.getModules()){
                            plannerModules.add(module.toString());
                        }
                        plannerModules.add("?");
                    }else{
                        plannerModules.add("NIL");
                    }
                }
                String jsonTerms = gson.toJson(terms);
                String jsonMods = gson.toJson( plannerModules );
                prefsEditor.putString(PlannerFragment.KEY_DATA_TERMS, jsonTerms);
                prefsEditor.putString(PlannerFragment.KEY_DATA_MODS, jsonMods);
                prefsEditor.apply();
                Intent resultIntent = new Intent();
                setResult( Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
