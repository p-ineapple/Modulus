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
    List<PlannerModel> editPlannerList = PlannerFragment.plannerList;
    PlannerModel selectedPlanner;
    MaterialCardView selectTermCard; TextView tvTerm;
    MaterialCardView selectElectivesCard; TextView tvElectives; String[] electivesList;
    MaterialCardView selectHASSCard; TextView tvHASS; String[] HASSList;
    boolean[] selectedElectives;
    ArrayList<String> selectedModulesIndex = new ArrayList<>();
    String[] filteredModulesList;
    ImageView backButton;
    Button confirmButton;
    SharedPreferences mPreferences;
    final static String KEY_NAME = "Modules";
    final static String KEY_PATH = "Term";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropdown_menu);

        selectTermCard = findViewById(R.id.selectTermCard);
        tvTerm = findViewById(R.id.tvTerm);

        selectTermCard.setOnClickListener(v -> {
            showTermDialog();
        });

        selectElectivesCard = findViewById(R.id.selectElectivesCard);
        tvElectives = findViewById(R.id.tvElectives);

        selectElectivesCard.setOnClickListener(v -> {
            showElectivesDialog();
        });

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

                String updateModules = tvElectives.getText().toString();
                System.out.println(updateModules);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_NAME, updateModules);
                resultIntent.putExtra(KEY_PATH, selectedPlanner.getTerm());

//                SharedPreferences.Editor prefsEditor = mPreferences.edit();
//                Gson gson = new Gson();
//                ArrayList<String> terms = new ArrayList<>();
//                ArrayList<String> plannerModules = new ArrayList<>();
//                for (PlannerModel planner: editPlannerList) {
//                    terms.add(planner.getTerm());
//                    if (planner.getModules() != null){
//                        for(ModuleModel module: planner.getModules()){
//                            plannerModules.add(module.toString());
//                        }
//                        plannerModules.add("?");
//                    }else{
//                        plannerModules.add("NIL");
//                    }
//                }
//                String jsonTerms = gson.toJson(terms);
//                String jsonMods = gson.toJson( plannerModules );
//                prefsEditor.putString(PlannerFragment.KEY_DATA_TERMS, jsonTerms);
//                prefsEditor.putString(PlannerFragment.KEY_DATA_MODS, jsonMods);
//                prefsEditor.apply();

                setResult( Activity.RESULT_OK, resultIntent);
                finish();
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
            selectedElectives = new boolean[filteredModulesList.length];

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
                    boolean checkCount;
                    if(term == 7 || term == 8){
                        checkCount = (count > 3);
                    }else{
                        checkCount = (count > 4);
                    }
                    if(checkCount) {
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
                    StringBuilder s = new StringBuilder();
                    for(int i = 0; i < selectedModulesIndex.size(); i++){
                        s.append(filteredModulesList[Integer.parseInt(selectedModulesIndex.get(i))]);
                        if(i != selectedModulesIndex.size() - 1){
                            s.append("\n");
                        }
                    }
                    tvElectives.setText(s.toString());
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
        for (PlannerModel planner : editPlannerList) {
            if(planner.getTerm().equals(term))
                return planner;
        }
        Log.d("Edit", "No Planner");
        return null;
    }

    private void setValues() {
        TextView tv = findViewById(R.id.tvTerm);
        tv.setText(selectedPlanner.getTerm());
        List<String> stringFilteredModulesList = new ArrayList<String>();
        List<String> stringHASSList = new ArrayList<String>();
        List<String> stringElectivesList = new ArrayList<String>();
        for(ModuleModel module: InsightsFragment.moduleList){
            if( module.getTerm().contains(String.valueOf(selectedPlanner.getTermInt())) ){
                stringFilteredModulesList.add(module.toString());
                if(module.getTags().contains("HASS")){
                    stringHASSList.add(module.toString());
                }else{
                    stringElectivesList.add(module.toString());
                }
            }
        }
        CardView capstoneCard = findViewById(R.id.capstoneCard);
        if(selectedPlanner.getTerm().contains("7") || selectedPlanner.getTerm().contains("8")){
            capstoneCard.setVisibility(View.VISIBLE);
        }else{
            capstoneCard.setVisibility(View.GONE);
        }
        filteredModulesList = stringFilteredModulesList.toArray(new String[0]);
        HASSList = stringHASSList.toArray(new String[0]);
        electivesList = stringElectivesList.toArray(new String[0]);
    }
}
