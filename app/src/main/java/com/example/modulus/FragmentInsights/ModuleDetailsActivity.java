package com.example.modulus.FragmentInsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;

public class ModuleDetailsActivity extends AppCompatActivity{
    ModuleModel selectedModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_details);
        getSelectedModule();
        setValues();
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
        TextView tv = (TextView) findViewById(R.id.moduleDetailsIDName);
        tv.setText(selectedModule.toString());
    }
}