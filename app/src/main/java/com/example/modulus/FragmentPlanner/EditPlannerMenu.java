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

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class EditPlannerMenu extends AppCompatActivity {
    List<PlannerModel> basePlannerList = PlannerFragment.basePlannerList;
    PlannerModel selectedPlanner;
    MaterialCardView selectTermCard; TextView tvTerm;
    int electiveLimit;
    CardView coreCard; TextView tvCore;
    MaterialCardView selectElectivesCard; TextView tvElectives; String[] electivesList;
    CardView term3HASSCard; TextView tvTerm3HASS;
    MaterialCardView selectHASSCard; TextView tvHASS; String[] HASSList;
    boolean[] selectedElectives;
    boolean changed;
    ArrayList<String> selectedModulesIndex = new ArrayList<>();
    String[] filteredModulesList;
    ImageView backButton;
    Button confirmButton;
    SharedPreferences mPreferences;
    final static String KEY_NAME = "Electives";
    final static String KEY_PATH = "Term";
    final String TAG = "Edit Planner";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropdown_menu);
        changed = false;

        selectTermCard = findViewById(R.id.selectTermCard);
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

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult( Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        mPreferences = this.getSharedPreferences(PlannerFragment.PREF_FILE, Context.MODE_PRIVATE);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changed){
                    Toast.makeText(EditPlannerMenu.this, "No Changes Detected", Toast.LENGTH_SHORT).show();;
                }
                else{
                    StringBuilder updatedModules = new StringBuilder();
                    if(coreCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvCore.getText().toString()).append("\n");
                        System.out.println("HELP" + updatedModules);
                        Log.d(TAG, "core");
                    }
                    if(selectElectivesCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvElectives.getText().toString()).append("\n");
                        System.out.println("HELP" + updatedModules);
                        Log.d(TAG, "elective");
                    }
                    if(term3HASSCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvTerm3HASS.getText().toString()).append("\n");
                        System.out.println("HELP" + updatedModules);
                        Log.d(TAG, "term3");
                    }
                    if(selectHASSCard.getVisibility() == View.VISIBLE){
                        updatedModules.append(tvHASS.getText().toString()).append("\n");
                        System.out.println("HELP" + updatedModules);
                        Log.d(TAG, "hass");
                    }
                    System.out.println(updatedModules);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenu.this);

        builder.setTitle("Select Term");
        builder.setCancelable(false);

        String[] terms = new String[]{"Term 3", "Term 4", "Term 5", "Term 6", "Term 7", "Term 8"};
        builder.setSingleChoiceItems(terms, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvTerm.setText(terms[which]);
                selectedPlanner = getSelectedPlanner(terms[which]);
                setValues();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenu.this);

            builder.setTitle("Select Electives");
            builder.setCancelable(false);

            builder.setMultiChoiceItems(electivesList, selectedElectives, new DialogInterface.OnMultiChoiceClickListener() {
                int count = 0;
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        selectedModulesIndex.add(String.valueOf(which));
                    }else{
                        selectedModulesIndex.remove(String.valueOf(which));
                    }
                    count += isChecked ? 1 : -1;
                    int term = selectedPlanner.getTermInt();
                    if(count > electiveLimit) {
                        Toast.makeText(EditPlannerMenu.this, "Limit Reached!.", Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenu.this);

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
            if(planner.getTerm().equals(term))
                return planner;
        }
        Log.d("Edit", "No Planner");
        return null;
    }

    private void setValues() {
        TextView tv = findViewById(R.id.tvTerm);
        tv.setText(selectedPlanner.getTerm());

        //filter available
        List<String> stringFilteredModulesList = new ArrayList<String>();
        List<String> stringHASSList = new ArrayList<String>();
        List<String> stringElectivesList = new ArrayList<String>();
        for(ModuleModel module: InsightsFragment.moduleList){
            if(!selectedPlanner.getModules().toString().contains(module.toString())){
                if( module.getTerm().contains(String.valueOf(selectedPlanner.getTermInt())) ){
                    stringFilteredModulesList.add(module.toString());
                    if(module.getTags().contains("HASS")){
                        stringHASSList.add(module.toString());
                    }else{
                        stringElectivesList.add(module.toString());
                    }
                }
            }
        }
        filteredModulesList = stringFilteredModulesList.toArray(new String[0]);
        HASSList = stringHASSList.toArray(new String[0]);
        electivesList = stringElectivesList.toArray(new String[0]);

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
}
