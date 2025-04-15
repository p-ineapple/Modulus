package com.example.modulus.FragmentInsights;

import static com.example.modulus.R.id.backgroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;
import com.example.modulus.Utils.WebView;

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
        setContentView(R.layout.insights_activity_module_details);
        getSelectedModule();
        setValues();

        RelativeLayout backButton = findViewById(backgroup);
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



        TextView Name = findViewById(R.id.moduleDetailsName);
        Name.setText(selectedModule.getName());

        TextView pillar = findViewById(R.id.moduleDetailsPillar);
        pillar.setText(selectedModule.getPillar() + " " + selectedModule.getId());
        int color = ContextCompat.getColor(this, selectedModule.getColor());
        pillar.setTextColor(color);


        TextView tags = findViewById(R.id.tags);
        tags.setText(String.join(", ", selectedModule.getTags()));


        RelativeLayout back = findViewById(R.id.backgroup);
        back.setBackgroundColor(color);

        TextView moreInfo = findViewById(R.id.moreInfo);
        moreInfo.setCompoundDrawableTintList(ColorStateList.valueOf(color));

        ImageView imageMod = findViewById(R.id.imageMod);
        imageMod.setImageDrawable(selectedModule.getImage());




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
                        orString.append(" <b>OR</b><br />");
                    }
                }
                output.add(orString.toString());
            }


        }

        if (!output.isEmpty()){
            preReq.setText(Html.fromHtml(TextUtils.join("<br>", output), Html.FROM_HTML_MODE_LEGACY));

        } else{
            preReq.setText("No Pre-requisites");
        }



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