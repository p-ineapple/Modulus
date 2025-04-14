package com.example.modulus.FragmentPlanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modulus.Adapter.PlannerAdapter;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.FragmentInsights.DataBaseHelperInsights;
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlannerFragment extends Fragment {
    public static List<PlannerModel> plannerList;
    PlannerAdapter adapter;
    RecyclerView recyclerView;
    DataBaseHelperInsights myDB;
    ImageView editButton;
    SharedPreferences mPreferences;
    static final String KEY_DATA_PILLAR = "SHARED_PREF_DATA_PILLAR";
    static final String KEY_DATA_TRACK = "SHARED_PREF_DATA_TRACK";
    static final String KEY_DATA_TERMS = "SHARED_PREF_DATA_TERMS";
    static final String KEY_DATA_MODS = "SHARED_PREF_DATA_MODS";
    static final String PREF_FILE = "mainsharedpref";
    private final String TAG = "PlannerModel";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        recyclerView = view.findViewById(R.id.plannerRecyclerView);

        myDB = new DataBaseHelperInsights(getContext());
        mPreferences = this.getActivity().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if(mPreferences.getString(KEY_DATA_TERMS, "").equals("")){
            Log.d(TAG, "New Account");
            plannerList = myDB.getPlanner("Default");
            System.out.println(plannerList);
        }else{
            onResume();
            Log.d(TAG, "Refresh");
        }
        if(InsightsFragment.moduleList == null){
            myDB = new DataBaseHelperInsights(getContext());
            InsightsFragment.moduleList = myDB.getAllModules();
        }
        adapter = new PlannerAdapter(plannerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        LinearLayout editTab = view.findViewById(R.id.editTab);
        editTab.setVisibility(View.GONE);
        editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTab.getVisibility() == View.VISIBLE){
                    editTab.setVisibility(View.GONE);
                }else{
                    editTab.setVisibility(View.VISIBLE);
                }
            }
        });

        Button pillar = view.findViewById(R.id.pillar);
        TextView pillarText = view.findViewById(R.id.pillarText);
        String pillarPref = mPreferences.getString(KEY_DATA_PILLAR, "");
        if(!pillarPref.isEmpty()){
            pillarText.setText(pillarPref);
        }
        TextView trackText = view.findViewById(R.id.specializationText);
        String trackPref = mPreferences.getString(KEY_DATA_TRACK, "");
        if(!trackPref.isEmpty()){
            trackText.setText(trackPref);
        }
        TextView minorText = view.findViewById(R.id.minorText);
        String minorPref = mPreferences.getString(KEY_DATA_TRACK, "");
        if(!trackPref.isEmpty()){
            trackText.setText(trackPref);
        }
        String[] pillars = new String[]{"ASD", "CSD", "DAI", "EPD", "ESD"};
        pillar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Select a Pillar");
                mBuilder.setSingleChoiceItems(pillars, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pillarText.setText(pillars[which]);
                        plannerList = myDB.getPlanner(pillars[which]);
                        recyclerView.setAdapter(adapter);
                        SharedPreferences.Editor prefsEditor = mPreferences.edit();
                        prefsEditor.putString(PlannerFragment.KEY_DATA_PILLAR, pillars[which]);
                        if(pillars[which].equals("ASD") || pillars[which].equals("DAI")){
                            trackText.setText("No Specialization");
                            prefsEditor.putString(PlannerFragment.KEY_DATA_TRACK, "No Specialization");
                        }
                        prefsEditor.apply();
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        Button track = view.findViewById(R.id.track);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pillarText.getText().equals("Pillar")){
                    Toast.makeText(getContext(), "Select Pillar First!", Toast.LENGTH_SHORT).show();
                }else if(pillarText.getText().equals("ASD") || pillarText.getText().equals("DAI")){
                    Toast.makeText(getContext(), "No Tracks Available", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "hi!", Toast.LENGTH_SHORT).show();
//                    String
//                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
//                    mBuilder.setTitle("Select a Specialization");
//                    mBuilder.setSingleChoiceItems(pillars, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            trackText.setText(pillars[which]);
////                            plannerList = myDB.getPlanner(pillars[which]);
//                            dialog.dismiss();
//                        }
//                    });
//                    mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    AlertDialog mDialog = mBuilder.create();
//                    mDialog.show();
                }
            }
        });

        Button edit = view.findViewById(R.id.planTerm);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Edit");
                Intent showDetail = new Intent(getContext(), EditPlanner.class);
                startActivity(showDetail);
            }
        });

        return view;
    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        SharedPreferences.Editor prefsEditor = mPreferences.edit();
//        Gson gson = new Gson();
//        ArrayList<String> terms = new ArrayList<>();
//        ArrayList<String> plannerModules = new ArrayList<>();
//        for (PlannerModel planner: plannerList) {
//            terms.add(planner.getTerm());
//            if (planner.getModules() != null){
//                for(ModuleModel module: planner.getModules()){
//                    plannerModules.add(module.toString());
//                }
//                plannerModules.add("?");
//            }else{
//                plannerModules.add("NIL");
//            }
//        }
//        String jsonTerms = gson.toJson(terms);
//        String jsonMods = gson.toJson( plannerModules );
//        prefsEditor.putString(KEY_DATA_TERMS, jsonTerms);
//        prefsEditor.putString(KEY_DATA_MODS, jsonMods);
//        prefsEditor.apply();
//    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "Resume");
//        Gson gson = new Gson();
//        String jsonTerms = mPreferences.getString(KEY_DATA_TERMS, "");
//        String jsonMods = mPreferences.getString(KEY_DATA_MODS, "");
//        ArrayList<String> terms = gson.fromJson(jsonTerms, ArrayList.class);
//        ArrayList<String> mods = gson.fromJson(jsonMods, ArrayList.class);
//        System.out.println(terms);
//        System.out.println(mods);
//        if(terms != null && mods != null){
//            plannerList = new ArrayList<>();
//            int modsPointer = 0;
//            for (int i = 0; i< terms.size(); i++) {
//                PlannerModel planner = new PlannerModel(terms.get(i));
//                List<ModuleModel> plannerModules = new ArrayList<ModuleModel>();
//                for(int j = modsPointer; j < mods.size(); j++){
//                    if(mods.get(j).equals("?")){
//                        planner.setModules(plannerModules);
//                        plannerList.add(planner);
//                        modsPointer++;
//                        break;
//                    }else if(mods.get(j).equals("NIL")){
//                        plannerList.add(planner);
//                        modsPointer++;
//                        break;
//                    }else {
//                        plannerModules.add(ModuleModel.getModuleFromString(mods.get(j)));
//                        modsPointer++;
//                    }
//                }
//            }
//            Log.d(TAG, "Updated");
//        }


        String pillarPref = mPreferences.getString(KEY_DATA_PILLAR, "");
        plannerList = myDB.getPlanner(pillarPref);
        recyclerView.setAdapter(adapter);

        recyclerView.setAdapter(adapter);
    }
}