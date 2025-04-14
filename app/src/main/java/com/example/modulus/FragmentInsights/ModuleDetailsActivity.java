package com.example.modulus.FragmentInsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;
import com.example.modulus.Utils.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModuleDetailsActivity extends AppCompatActivity{
    ModuleModel selectedModule;
    final String TAG = "Module Insights";

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

        TextView info = findViewById(R.id.moreInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WebView.isNetworkAvailable(ModuleDetailsActivity.this)) {
                    openURL();
                } else {
                    Toast.makeText(ModuleDetailsActivity.this, "No Network", Toast.LENGTH_SHORT).show();
                }
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
        List<String> modPreReq = selectedModule.getPrerequisites();
        List<String> output = new ArrayList<>();
        for(String id: modPreReq){
            if(!id.contains("/")){
                ModuleModel module = InsightsFragment.moduleList.stream().filter(m -> id.contains(m.getId())).findFirst().orElse(null);
                if(module != null){
                    output.add(module.toString());
                }
            }else{
                String[] splitMods = id.split("/");
                StringBuilder orString = new StringBuilder();
                for(int i = 0; i < splitMods.length; i++){
                    String innerId = splitMods[i];
                    ModuleModel module = InsightsFragment.moduleList.stream().filter(m -> innerId.contains(m.getId())).findFirst().orElse(null);
                    if(module != null) {
                        orString.append(module);
                    }
                    if(i < splitMods.length - 1){
                        orString.append(" or ");
                    }
                }
                output.add(orString.toString());
            }
        }
        preReq.setText("Pre-Requisites: \n - " + String.join("\n - ", output));
    }

    private void openURL(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Looper uiLooper = Looper.getMainLooper();
        final Handler handler = new Handler(uiLooper);

        executorService.execute(new Runnable() {
            @Override
            public void run() { // Background thread
                URL url = WebView.getURLFromDesc(selectedModule);
                String response = WebView.getJson(url);
                if (response == null) {
                    handler.post(new Runnable() { // Main thread
                        @Override
                        public void run() {
                            // main thread
                            Log.i("UI Thread", "Invalid URL");
                            Toast.makeText(ModuleDetailsActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        // main thread
                        @Override
                        public void run() {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedModule.getDescription()));
                            startActivity(browserIntent);
                        }
                    });
                }
            }
        });
    }
}