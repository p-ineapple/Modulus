package com.example.modulus.FragmentInsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;

import java.util.Arrays;
import java.util.List;

public class ModuleDetailsActivity extends AppCompatActivity{
    ModuleModel selectedModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_details);
        getSelectedModule();
        setValues();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSelectedModule() {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("id");
        selectedModule = getParsedModule(parsedStringID);
    }

    private ModuleModel getParsedModule(String parsedID) {
        for (ModuleModel module : InsightsFragment.moduleList) {
            if(module.getId().equals(parsedID))
                return module;
        }
        return null;
    }

    private void setValues() {
        TextView idName = findViewById(R.id.moduleDetailsIDName);
        idName.setText(selectedModule.toString());
        TextView preReq = findViewById(R.id.prerequisites);
        String[] modPreReq = selectedModule.getPrerequisites().toArray(new String[0]);
        preReq.setText("Pre-Requisites: \n - " + String.join("\n - ", modPreReq));
    }
}