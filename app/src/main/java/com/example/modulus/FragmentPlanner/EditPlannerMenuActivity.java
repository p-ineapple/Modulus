package com.example.modulus.FragmentPlanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.modulus.FragmentInsights.DataBaseHelperInsights;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.R;
import com.example.modulus.Utils.ModuleGraph;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class EditPlannerMenuActivity extends AppCompatActivity {
    private final List<PlannerModel> basePlannerList = PlannerFragment.basePlannerList;
    private final List<PlannerModel> mPlannerList = PlannerFragment.mPlannerList;
    private ModuleGraph<String> g;
    private PlannerModel selectedPlanner;
    private List<String> modulesTillTerm;
    private TextView tvTerm;
    private int electiveLimit;
    private CardView coreCard; private TextView tvCore;
    private MaterialCardView selectElectivesCard; private TextView tvElectives; private String[] electivesList;
    private CardView term3HASSCard; private TextView tvTerm3HASS;
    private MaterialCardView selectHASSCard; private TextView tvHASS; private String[] HASSList;
    private boolean[] selectedElectives;
    private boolean changed;
    private ArrayList<String> selectedModulesIndex = new ArrayList<>();
    private String[] filteredModulesList;
    private SharedPreferences mPreferences;
    public final static String KEY_NAME = "Electives";
    public final static String KEY_PATH = "Term";
    private final String TAG = "Edit Planner";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_dropdown_menu);
        changed = false;

        DataBaseHelperInsights myDB = new DataBaseHelperInsights(this);
        g = myDB.getGraph();

        MaterialCardView selectTermCard = findViewById(R.id.selectTermCard);
        tvTerm = findViewById(R.id.tvTerm);
        selectTermCard.setOnClickListener(v -> {
            showTermDialog();
        });

        coreCard = findViewById(R.id.coreCard);
        tvCore = findViewById(R.id.tvCore);
        selectElectivesCard = findViewById(R.id.selectElectivesCard);
        tvElectives = findViewById(R.id.tvElectives);
        selectElectivesCard.setOnClickListener(v -> {
            showElectivesDialog();
        });

        term3HASSCard = findViewById(R.id.term3HASS);
        tvTerm3HASS = findViewById(R.id.tvTerm3HASS);
        selectHASSCard = findViewById(R.id.selectHASSCard);
        tvHASS = findViewById(R.id.tvHASS);
        selectHASSCard.setOnClickListener(v -> {
            showHASSDialog();
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult( Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        mPreferences = this.getSharedPreferences(PlannerFragment.PREF_FILE, Context.MODE_PRIVATE);
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changed){
                    Toast.makeText(EditPlannerMenuActivity.this, "No Changes Detected", Toast.LENGTH_SHORT).show();;
                }
                else{
                    StringBuilder updatedModules = new StringBuilder();
                    if(coreCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvCore.getText().toString()).append("\n");
                        Log.d(TAG, "core" + updatedModules);
                    }
                    if(selectElectivesCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvElectives.getText().toString()).append("\n");
                        Log.d(TAG, "elective" + updatedModules);
                    }
                    if(term3HASSCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvTerm3HASS.getText().toString()).append("\n");
                        Log.d(TAG, "term3" + updatedModules);
                    }
                    if(selectHASSCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvHASS.getText().toString()).append("\n");
                        Log.d(TAG, "hass" + updatedModules);
                    }
                    Log.d(TAG, "Updated modules: " + updatedModules);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_NAME, updatedModules.toString());
                    resultIntent.putExtra(KEY_PATH, selectedPlanner.getTerm());

                    setResult( Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private void showTermDialog(){
        tvTerm = findViewById(R.id.tvTerm);
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenuActivity.this);

        builder.setTitle("Select Term");
        builder.setCancelable(false);

        String[] terms = new String[]{"Term 3", "Term 4", "Term 5", "Term 6", "Term 7", "Term 8"};
        builder.setSingleChoiceItems(terms, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvTerm.setText(terms[which]);
                selectedPlanner = getSelectedPlanner(terms[which]);
                setValues();
                modulesTillTerm = getModulesTillTerm(terms[which]);
                dialog.dismiss();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }
    private void showElectivesDialog(){
        if(selectedPlanner == null){
            Toast.makeText(this, "Select a Term first!", Toast.LENGTH_SHORT).show();
        }else{
            selectedElectives = new boolean[electivesList.length];

            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenuActivity.this);

            builder.setTitle("Select Electives");
            builder.setCancelable(false);

            builder.setMultiChoiceItems(electivesList, selectedElectives, new DialogInterface.OnMultiChoiceClickListener() {
                int count = 0;
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    boolean added = false;
                    if(isChecked){
                        String id = electivesList[which].split(" ")[0]; // String ID of module tapped
                        if(checkPreReq(id)){
                            selectedModulesIndex.add(String.valueOf(which));
                            added = true;
                        }else{
                            Toast.makeText(EditPlannerMenuActivity.this, "Pre-Requisites not met: " + g.neighbours(id).toString(), Toast.LENGTH_SHORT).show();
                            selectedElectives[which] = false;
                            selectedModulesIndex.remove(String.valueOf(which));
                            ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                        }
                    }else{
                        selectedModulesIndex.remove(String.valueOf(which));
                    }
                    count += added ? 1 : -1;
                    if(count > electiveLimit) {
                        Toast.makeText(EditPlannerMenuActivity.this, "Limit Reached!", Toast.LENGTH_SHORT).show();
                        selectedElectives[which] = false;
                        selectedModulesIndex.remove(String.valueOf(which));
                        count--;
                        ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                    }
                }
            }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvElectives.setText("");
                    StringBuilder s = new StringBuilder();
                    for(int i = 0; i < selectedModulesIndex.size(); i++){
                        s.append(electivesList[Integer.parseInt(selectedModulesIndex.get(i))]);
                        if(i != selectedModulesIndex.size() - 1){
                            s.append("\n");
                        }
                    }
                    tvElectives.setText(s.toString());
                    changed = true;
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(int i = 0; i < selectedElectives.length; i++){
                        selectedElectives[i] = false;
                    }
                    selectedModulesIndex.clear();
                    tvElectives.setText("");
                }
            });
            AlertDialog mDialog = builder.create();
            mDialog.show();
        }
    }

    private void showHASSDialog(){
        if(selectedPlanner == null){
            Toast.makeText(this, "Select a Term first!", Toast.LENGTH_SHORT).show();
        }else{
            tvHASS = findViewById(R.id.tvHASS);
            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenuActivity.this);

            builder.setTitle("Select HASS");
            builder.setCancelable(false);

            builder.setSingleChoiceItems(HASSList, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvHASS.setText(HASSList[which]);
                    changed = true;
                    dialog.dismiss();
                }
            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog mDialog = builder.create();
            mDialog.show();
        }
    }

    private PlannerModel getSelectedPlanner(String term) {
        for (PlannerModel planner : basePlannerList) {
            if(planner.getTerm().equals(term)){
                return planner;
            }
        }
        Log.d(TAG, "No Planner");
        return null;
    }

    private List<String> getModulesTillTerm(String term){
        List<String> modsSoFar = new ArrayList<>();
        for (PlannerModel planner : mPlannerList) {
            for(ModuleModel m: planner.getModules()){
                modsSoFar.add(m.getId());
            }
            if(planner.getTerm().equals(term)){
                return modsSoFar;
            }
        }
        return modsSoFar;
    }

    private void setValues() {
        TextView tv = findViewById(R.id.tvTerm);
        tv.setText(selectedPlanner.getTerm());

        List<ModuleModel> core = new ArrayList<>();
        List<ModuleModel> rec = new ArrayList<>();
        if(PlannerFragment.trackModel != null){
            core = PlannerFragment.trackModel.getCore();
            rec = PlannerFragment.trackModel.getRecMods();
        }

        //filter available
        List<String> stringFilteredModulesList = new ArrayList<String>();
        List<String> stringHASSList = new ArrayList<String>();
        List<String> stringCoreList = new ArrayList<String>();
        List<String> stringElectivesList = new ArrayList<String>();
        for(ModuleModel module: InsightsFragment.moduleList){
            if(!selectedPlanner.getModules().toString().contains(module.toString())){
                if( module.getTerm().contains(String.valueOf(selectedPlanner.getTermInt())) ){
                    stringFilteredModulesList.add(module.toString());
                    if(module.getTags().contains("HASS")){
                        stringHASSList.add(module.toString());
                    }else if( (!core.isEmpty() && core.toString().contains(module.toString())) ||
                            (!rec.isEmpty() && rec.toString().contains(module.toString()))){
                        stringCoreList.add(module.toString());
                    }else{
                        stringElectivesList.add(module.toString());
                    }
                }
            }
        }
        filteredModulesList = stringFilteredModulesList.toArray(new String[0]);
        HASSList = stringHASSList.toArray(new String[0]);
        stringCoreList.addAll(stringElectivesList);
        electivesList = stringCoreList.toArray(new String[0]);

        //set values
        electiveLimit = 3;
        List<ModuleModel> plannerModulesList = selectedPlanner.getModules();
        if(plannerModulesList != null) {
            StringBuilder setTvCore = new StringBuilder();
            StringBuilder setHASSCore = new StringBuilder();
            for (ModuleModel module : plannerModulesList) {
                if (module.getTags().contains("HASS")) {
                    setHASSCore.append(module.toString());
                } else{
                    setTvCore.append(module.toString()).append("\n");
                    electiveLimit -= 1;
                }
            }
            if (setTvCore.length() > 0) {
                coreCard.setVisibility(View.VISIBLE);
                tvCore.setText(setTvCore.toString().substring(0, setTvCore.length()-1));
                if(electiveLimit == 0){
                    selectElectivesCard.setVisibility(View.GONE);
                }else{
                    selectElectivesCard.setVisibility(View.VISIBLE);
                }
            }else{
                coreCard.setVisibility(View.GONE);
                tvCore.setText("Core");
            }

            if (setHASSCore.length() > 0) {
                term3HASSCard.setVisibility(View.VISIBLE);
                tvTerm3HASS.setText(setHASSCore.toString());
                selectHASSCard.setVisibility(View.GONE);
            }else{
                term3HASSCard.setVisibility(View.GONE);
                tvTerm3HASS.setText("Term 3 HASS");
                selectHASSCard.setVisibility(View.VISIBLE);
            }

        }
    }

    private boolean checkPreReq(String id){
        String cost = g.getCost(id);
        if(cost.equals("NIL")){ // Base Case: no preReq
            Log.d(TAG, "no preReq");
            return true;
        }
        List<String> neighbours = g.neighbours(id);
        if(cost.equals("Soft")){ // Recursive Case: soft preReq, any 1 will return true
            for(String s: neighbours){
                if(modulesTillTerm.contains(s)){
                    if(checkPreReq(s)){ // Recursively check preReq
                        Log.d(TAG, "check successful");
                        return true;
                    }
                }
            }
            Log.d(TAG, "check successful");
            return false;
        }else{ // // Recursive Case: hard preReq, must have all to return true,
            // neighbours may have "/", which always has 2 mods, of these having either will count
            int count = neighbours.size();
            for(String s: neighbours){
                if(s.contains("/")){
                    String[] softMods = s.split("/");
                    for(String soft: softMods){ // Check if either mod is in modsTillTerm
                        if(modulesTillTerm.contains(soft)){
                            if(checkPreReq(s)){ // Recursively check preReq
                                count -= 1;
                                break;
                            }
                        }
                    }
                }else{
                    for(String s1: modulesTillTerm){
                        if(s.equals(s1)){
                            if(checkPreReq(s)) { // Recursively check preReq
                                count -= 1;
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "check successful");
            return count == 0;
        }
    }
}
