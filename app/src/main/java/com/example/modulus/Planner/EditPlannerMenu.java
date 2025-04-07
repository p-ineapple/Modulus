package com.example.modulus.Planner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modulus.Class.Module;
import com.example.modulus.Class.Planner;
import com.example.modulus.Insights.InsightsFragment;
import com.example.modulus.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class EditPlannerMenu extends AppCompatActivity {
    Planner selectedPlanner;
    MaterialCardView selectCard;
    TextView tvModules;
    boolean[] selectedModules;
    ArrayList<String> selectedModulesIndex = new ArrayList<>();
    String[] filteredModulesList;
//    List<Module> actualFilteredModulesList;
    Button confirmButton;
    final static String KEY_NAME = "Modules";
    final static String KEY_PATH = "Term";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropdown_menu);

        selectCard = findViewById(R.id.selectCard);
        tvModules = findViewById(R.id.tvModules);
        confirmButton = findViewById(R.id.confirmButton);

        getSelectedPlanner();
        setValues();

        selectedModules = new boolean[filteredModulesList.length];

        selectCard.setOnClickListener(v -> {
            showModuleDialog();
        });
    }

    private void showModuleDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPlannerMenu.this);

        builder.setTitle("Select Modules");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(filteredModulesList, selectedModules, new DialogInterface.OnMultiChoiceClickListener() {
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
                    selectedModules[which] = false;
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
                tvModules.setText(s.toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < selectedModules.length; i++){
                    selectedModules[i] = false;
                }
                selectedModulesIndex.clear();
                tvModules.setText("");
            }
        });
        builder.show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateModules = tvModules.getText().toString();
                System.out.println(updateModules);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_NAME, updateModules);
                resultIntent.putExtra(KEY_PATH, selectedPlanner.getTerm());
                setResult( Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    private void getSelectedPlanner() {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("id");
        selectedPlanner = getParsedModule(parsedStringID);
    }

    private Planner getParsedModule(String parsedID) {
        for (Planner planner : PlannerFragment.plannerList) {
            if(planner.getTerm().equals(parsedID))
                return planner;
        }
        return null;
    }

    private void setValues() {
        TextView tv = findViewById(R.id.term);
        tv.setText(selectedPlanner.getTerm());
//        actualFilteredModulesList = new ArrayList<Module>();
        List<String> stringFilteredModulesList = new ArrayList<String>();
        for(Module module: InsightsFragment.moduleList){
            if( module.getTerm().contains(String.valueOf(selectedPlanner.getTermInt())) ){
//                actualFilteredModulesList.add(module);
                stringFilteredModulesList.add(module.toString());
            }
        }
        filteredModulesList = stringFilteredModulesList.toArray(new String[0]);
    }
}
