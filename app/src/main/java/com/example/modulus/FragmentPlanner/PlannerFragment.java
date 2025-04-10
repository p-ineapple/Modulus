package com.example.modulus.FragmentPlanner;

import android.content.Context;
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
import android.widget.ImageView;

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
    RecyclerView recyclerView;
    DataBaseHelperInsights myDB;
    ImageView editButton;
    SharedPreferences mPreferences;
    static final String KEY_DATA_TERMS = "SHARED_PREF_DATA_TERMS";
    static final String KEY_DATA_MODS = "SHARED_PREF_DATA_MODS";
    static final String PREF_FILE = "mainsharedpref";
    private final String TAG = "PlannerModel";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Edit");
                Intent showDetail = new Intent(getContext(), EditPlanner.class);
                startActivity(showDetail);
            }
        });

        recyclerView = view.findViewById(R.id.plannerRecyclerView);

        mPreferences = this.getActivity().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if(mPreferences.getString(KEY_DATA_TERMS, "").equals("")){
            Log.d(TAG, "New Account");
            myDB = new DataBaseHelperInsights(getContext());
            plannerList = myDB.getPlanner();
            System.out.println(plannerList);
        }else{
            onResume();
            Log.d(TAG, "Refresh");
        }
        if(InsightsFragment.moduleList == null){
            myDB = new DataBaseHelperInsights(getContext());
            InsightsFragment.moduleList = myDB.getAllModules();
        }
        PlannerAdapter adapter = new PlannerAdapter(plannerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
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
        Gson gson = new Gson();
        String jsonTerms = mPreferences.getString(KEY_DATA_TERMS, "");
        String jsonMods = mPreferences.getString(KEY_DATA_MODS, "");
        ArrayList<String> terms = gson.fromJson(jsonTerms, ArrayList.class);
        ArrayList<String> mods = gson.fromJson(jsonMods, ArrayList.class);
        System.out.println(terms);
        System.out.println(mods);
        if(terms != null && mods != null){
            plannerList = new ArrayList<>();
            int modsPointer = 0;
            for (int i = 0; i< terms.size(); i++) {
                PlannerModel planner = new PlannerModel(terms.get(i));
                List<ModuleModel> plannerModules = new ArrayList<ModuleModel>();
                for(int j = modsPointer; j < mods.size(); j++){
                    if(mods.get(j).equals("?")){
                        planner.setModules(plannerModules);
                        plannerList.add(planner);
                        modsPointer++;
                        break;
                    }else if(mods.get(j).equals("NIL")){
                        plannerList.add(planner);
                        modsPointer++;
                        break;
                    }else {
                        plannerModules.add(ModuleModel.getModuleFromString(mods.get(j)));
                        modsPointer++;
                    }
                }
            }
            Log.d(TAG, "Updated");
        }
    }
}