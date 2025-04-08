package com.example.modulus.Insights;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.modulus.Class.FilterChip;
import com.example.modulus.Class.Module;
import com.example.modulus.Adapter.ModuleAdapter;
import com.example.modulus.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InsightsFragment extends Fragment {
    public static ArrayList<Module> moduleList;
    ArrayList<String> selectedFilters = new ArrayList<String>();
    SearchView search;
    String currentSearchText = "";
    RecyclerView modulesRecyclerView;
    ModuleAdapter.OnItemClickListener listener;
    ImageButton filterButton;
    ImageButton sortButton;
    ConstraintLayout sortTab;
    ArrayList<FilterChip> filterChips = new ArrayList<FilterChip>();
    final String TAG = "Browser";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        //set up modules modulesRecyclerView
        if (moduleList == null) {
            Log.d(TAG, "Setting up modules");
            setupData();
        }
        Log.d(TAG, "Database set up");
        modulesRecyclerView = view.findViewById(R.id.recyclerView);
        listener = new ModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Module module) {
                Intent showDetail = new Intent(getContext(), ModuleDetailsActivity.class);
                showDetail.putExtra("id", module.getId());
                startActivity(showDetail);
            }
        };
        modulesRecyclerView.setAdapter(new ModuleAdapter(moduleList, listener));
        modulesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //searchWidget
        search = view.findViewById(R.id.modulesListSearchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentSearchText = s;
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
                return false;
            }
        });
        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        sortTab = view.findViewById(R.id.sortTab);
        sortTab.setVisibility(View.VISIBLE);
        sortButton = view.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortTab.getVisibility() == View.VISIBLE){
                    sortTab.setVisibility(View.GONE);
                }else{
                    sortTab.setVisibility(View.VISIBLE);
                }
            }
        });
        Chip id = view.findViewById(R.id.id);
        Chip name = view.findViewById(R.id.name);
        Chip asc = view.findViewById(R.id.ascending);
        Chip des = view.findViewById(R.id.descending);
        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && asc.isChecked()) {
                    filteredModules.sort(Module.idAscending);
                } else if (id.isChecked() && des.isChecked()) {
                    filteredModules.sort(Module.idAscending);
                    Collections.reverse(filteredModules);
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (name.isChecked() && asc.isChecked()) {
                    filteredModules.sort(Module.nameAscending);
                } else if (name.isChecked() && des.isChecked()) {
                    filteredModules.sort(Module.nameAscending);
                    Collections.reverse(filteredModules);
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
            }
        });
        asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && asc.isChecked()) {
                    filteredModules.sort(Module.idAscending);
                } else if (name.isChecked() && asc.isChecked()) {
                    filteredModules.sort(Module.nameAscending);
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
            }
        });
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && des.isChecked()) {
                    filteredModules.sort(Module.idAscending);
                    Collections.reverse(filteredModules);
                } else if (name.isChecked() && des.isChecked()) {
                    filteredModules.sort(Module.nameAscending);
                    Collections.reverse(filteredModules);
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
            }
        });
        return view;
    }

    private void showFilterDialog() {
        Dialog filterDialog = new Dialog(this.getContext());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filterlayout);

        setUpFilterChips(filterDialog);
        for (FilterChip chipItem : filterChips) {
            Chip chip = chipItem.getChip();
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "click");
                    if (chip.isChecked()) {
                        chipItem.setChipCheck(true);
                        Filter.addFilter(selectedFilters, chipItem.getName());
                    } else {
                        chipItem.setChipCheck(false);
                        Filter.removeFilter(selectedFilters, chipItem.getName());
                    }
                }
            });
        }

        Button applyButton = filterDialog.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Module> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<Module>) filteredModules, listener));
                filterDialog.dismiss();
            }
        });

        Button resetButton = filterDialog.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilters.clear();
                for (FilterChip chip : filterChips) {
                    chip.setChipCheck(false);
                    chip.getChip().setChecked(false);
                }
            }
        });

        ImageView closeButton = filterDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void setupData() {
        DataBaseHelperInsights myDB = new DataBaseHelperInsights(getContext());
        moduleList = myDB.getAllModules();
    }
    public void setUpFilterChips(Dialog filterDialog) {
        List<String> filterChipNames = Arrays.asList(("ASD,ESD,EPD,DAI,ISTD,HASS,SMT," +
                "Term 1,Term 2,Term 3,Term 4,Term 5,Term 6,Term 7,Term 8," +
                "Core,Core Elective,Elective / Technical Elective,Freshmore Core,Freshmore Elective").split(","));
        ChipGroup pillarChips = filterDialog.findViewById(R.id.pillarChips);
        ChipGroup termChips = filterDialog.findViewById(R.id.termChips);
        ChipGroup courseChips = filterDialog.findViewById(R.id.courseChips);
        if (filterChips.isEmpty()) {
            for (int i = 0; i < filterChipNames.size(); i++) {
                FilterChip filterChip = new FilterChip(filterChipNames.get(i), false);
                filterChips.add(filterChip);
            }
        }
        for (int i = 0; i < filterChipNames.size(); i++) {
            Chip chip = new Chip(this.getContext());
            chip.setChipDrawable(ChipDrawable.createFromAttributes(this.getContext(), null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
            chip.setText(filterChipNames.get(i));
            chip.setCheckable(true);
            chip.setChecked(filterChips.get(i).isChipCheck());
            if (i < 7) {
                pillarChips.addView(chip);
            } else if (i < 15) {
                termChips.addView(chip);
            } else {
                courseChips.addView(chip);
            }
            filterChips.get(i).setChip(chip);
        }
        moduleList.sort(Module.idAscending);
    }
}

//    public void sortChips(View view){
//
//        if(idChip.isChecked() && ascChip.isChecked()){
//            MergeSort.mergeSortID(moduleList, moduleList.size());
//        }else if(idChip.isChecked() && descChip.isChecked()){
//            MergeSort.mergeSortID(moduleList, moduleList.size());
//            Collections.reverse(moduleList);
//        }else if(nameChip.isChecked() && ascChip.isChecked()){
//            MergeSort.mergeSortName(moduleList, moduleList.size());
//        }else if(nameChip.isChecked() && descChip.isChecked()){
//            MergeSort.mergeSortName(moduleList, moduleList.size());
//            Collections.reverse(moduleList);
//        }
//        Filter.checkForFilter();
//    }