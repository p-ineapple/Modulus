package com.example.modulus.FragmentPlanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.Model.TrackModel;
import com.example.modulus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlannerFragment extends Fragment {
    public static List<PlannerModel> plannerList;
    public static String myPillar;
    public static String myTrack;
    public static String myMinor;
    public static TrackModel trackModel;
    public static TrackModel minorModel;
    PlannerAdapter adapter;
    RecyclerView recyclerView;
    DataBaseHelperPlanner myDB;
    DataBaseHelperTracks tracksDB;
    ImageView editButton;
    SharedPreferences mPreferences;
    static final String KEY_DATA_PILLAR = "SHARED_PREF_DATA_PILLAR";
    static final String KEY_DATA_TRACK = "SHARED_PREF_DATA_TRACK";
    static final String KEY_DATA_MINOR = "SHARED_PREF_DATA_MINOR";
    static final String KEY_DATA_TERMS = "SHARED_PREF_DATA_TERMS";
    static final String KEY_DATA_MODS = "SHARED_PREF_DATA_MODS";
    static final String PREF_FILE = "mainsharedpref";
    private final String TAG = "PlannerModel";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        recyclerView = view.findViewById(R.id.plannerRecyclerView);

        myDB = new DataBaseHelperPlanner(getContext());
        tracksDB = new DataBaseHelperTracks(getContext());

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
            myDB = new DataBaseHelperPlanner(getContext());
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

        Button pillarButton = view.findViewById(R.id.pillarButton);
        TextView pillarText = view.findViewById(R.id.pillarText);
        String pillarPref = mPreferences.getString(KEY_DATA_PILLAR, "");
        if(!pillarPref.isEmpty()){
            pillarText.setText(pillarPref);
            myPillar = pillarPref;
        }
        TextView trackText = view.findViewById(R.id.specializationText);
        String trackPref = mPreferences.getString(KEY_DATA_TRACK, "");
        if(!trackPref.isEmpty()){
            trackText.setText(trackPref);
            myTrack = trackPref;
        }
        TextView minorText = view.findViewById(R.id.minorText);
        String minorPref = mPreferences.getString(KEY_DATA_MINOR, "");
        if(!minorPref.isEmpty()){
            minorText.setText(trackPref);
            myMinor = minorPref;
        }
        String[] pillars = new String[]{"ASD", "CSD", "DAI", "EPD", "ESD"};
        pillarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Select a Pillar");
                mBuilder.setSingleChoiceItems(pillars, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pillarText.setText(pillars[which]);
                        myPillar = pillars[which];
                        plannerList = myDB.getPlanner(pillars[which]);
                        recyclerView.setAdapter(adapter);
                        SharedPreferences.Editor prefsEditor = mPreferences.edit();
                        prefsEditor.putString(PlannerFragment.KEY_DATA_PILLAR, pillars[which]);
                        trackText.setText("No Specialization");
                        prefsEditor.putString(PlannerFragment.KEY_DATA_TRACK, "No Specialization");
                        minorText.setText("No Minor");
                        prefsEditor.putString(PlannerFragment.KEY_DATA_MINOR, "No Minor");
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

        Button trackButton = view.findViewById(R.id.trackButton);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPillar == null){
                    Toast.makeText(getContext(), "Select Pillar First!", Toast.LENGTH_SHORT).show();
                }else if(myPillar.equals("ASD") || myPillar.equals("DAI")){
                    Toast.makeText(getContext(), "No Tracks Available", Toast.LENGTH_SHORT).show();
                }else {
                    String[] tracks = tracksDB.getTracks(myPillar);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Select a Specialization");
                    builder.setSingleChoiceItems(tracks, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            trackText.setText(tracks[which]);
                            myTrack = tracks[which];
                            trackModel = tracksDB.getTrackModel(tracks[which], pillarPref);
                            SharedPreferences.Editor prefsEditor = mPreferences.edit();
                            prefsEditor.putString(PlannerFragment.KEY_DATA_TRACK, tracks[which]);
                            prefsEditor.apply();
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
        });

        Button minorButton = view.findViewById(R.id.minorButton);
        minorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPillar == null){
                    Toast.makeText(getContext(), "Select Pillar First!", Toast.LENGTH_SHORT).show();
                }else {
                    String[] allMinors = tracksDB.getMinors();
                    ArrayList<String[]> allMinorsEligibility = tracksDB.getMinorsEligibility();
                    List<String> availableMinorsList = new ArrayList<>();

                    for(int i = 0; i < allMinorsEligibility.size(); i++){
                        List<String> eligibility = Arrays.asList(allMinorsEligibility.get(i));
                        if(eligibility.contains(myPillar)){
                            availableMinorsList.add(allMinors[i]);
                        }
                    }
                    availableMinorsList.add("No Minor");
                    String[] availableMinors = availableMinorsList.toArray(new String[0]);

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    mBuilder.setTitle("Select a Minor");
                    mBuilder.setSingleChoiceItems(availableMinors, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            minorText.setText(availableMinors[which]);
                            myMinor = availableMinors[which];
                            if(!myMinor.equals("No Minor")){
                                minorModel = tracksDB.getTrackModel(availableMinors[which], pillarPref);
                                SharedPreferences.Editor prefsEditor = mPreferences.edit();
                                prefsEditor.putString(PlannerFragment.KEY_DATA_MINOR, availableMinors[which]);
                                prefsEditor.apply();
                                dialog.dismiss();
                            }
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
            }
        });

        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Result from DataEntryActivity is obtained
                        // Get the data and insert it into datasource
                        Log.d("TAG", "Back to Planner");
                        Bundle b = result.getData().getExtras();
                        if(b != null){
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
                            System.out.println(newPlannerModules.toString());
                            Log.d(TAG, "Planners Updated");
                        }else{
                            Log.d(TAG, "Nothing to Update");
                        }
                    }
                }

        );

        Button edit = view.findViewById(R.id.planTermButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Edit");
                System.out.println(trackModel);
                Intent intent = new Intent(getContext(), EditPlannerMenu.class);
                launcher.launch(intent);
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
        if(!pillarPref.isEmpty()) {
            plannerList = myDB.getPlanner(pillarPref);
            adapter = new PlannerAdapter(plannerList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        };

        recyclerView.setAdapter(adapter);
    }
}