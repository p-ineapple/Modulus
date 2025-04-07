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
import java.util.HashSet;
import java.util.List;

public class InsightsFragment extends Fragment {
    public static ArrayList<Module> moduleList;
    ArrayList<String> selectedFilters = new ArrayList<String>();
    SearchView search;
    String currentSearchText = "";
    DataBaseHelperInsights myDB;
    RecyclerView list;
    ModuleAdapter.OnItemClickListener listener;
    ImageButton filterButton;
    ImageButton sortButton;
    ConstraintLayout sortTab;
    ArrayList<FilterChip> filterChips = new ArrayList<FilterChip>();
    final String TAG = "Browser";

//    boolean sortHidden = true; boolean filterHidden = true; LinearLayout sortView; Button sortButton;
//    Button allButton, idAscButton, idDescButton, nameAscButton, nameDescButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        //set up modules list
        if (moduleList == null) {
            Log.d(TAG, "Setting up modules");
            setupData();
        }
        Log.d(TAG, "Database set up");
        list = view.findViewById(R.id.recyclerView);
        listener = new ModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Module module) {
                Intent showDetail = new Intent(getContext(), ModuleDetailsActivity.class);
                showDetail.putExtra("id", module.getId());
                startActivity(showDetail);
            }
        };
        ModuleAdapter adapter = new ModuleAdapter(moduleList, listener);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this.getContext()));

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
                ArrayList<Module> filteredModules = new ArrayList<Module>();
                for (Module module : moduleList) {
                    if (module.getName().toLowerCase().contains(s.toLowerCase()) || module.getId().contains(s)) {
                        if (selectedFilters.size() == 1) {
                            filteredModules.add(module);
                        } else {
                            for (String filter : selectedFilters) {
                                if (module.getName().toLowerCase().contains(filter) || module.getId().contains(s)) {
                                    filteredModules.add(module);
                                }
                            }
                        }
                    }
                }
                list.setAdapter(new ModuleAdapter(filteredModules, listener));
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
        selectedFilters.add("all");

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
//        sortChips(view);
        return view;
    }

    private void setupData() {
        myDB = new DataBaseHelperInsights(getContext());
        moduleList = myDB.getAllModules();
    }

    private void addFilter(String status) {
        if (status != null && !selectedFilters.contains(status)) {
            selectedFilters.add(status);
        }
    }

    private void removeFilter(String status) {
        selectedFilters.remove(status);
    }

    private void applyFilter() {
        ArrayList<Module> filteredModules = new ArrayList<Module>();
        List<String> filters = selectedFilters.subList(1,selectedFilters.size());
        List<String> pillars = Arrays.asList(("ASD,ESD,EPD,DAI,ISTD,HASS,SMT").split(","));
        List<String> terms = Arrays.asList(("Term 1,Term 2,Term 3,Term 4,Term 5,Term 6,Term 7,Term 8").split(","));
        List<String> courses = Arrays.asList(("Core,Core Elective,Elective / Technical Elective,Freshmore Core,Freshmore Elective").split(","));
        if(new HashSet<>(pillars).containsAll(filters) || new HashSet<>(terms).containsAll(filters)
                || new HashSet<>(courses).containsAll(filters)){
            for (Module module : moduleList) {
                for (String filter : selectedFilters) {
                    if (module.getTags().contains(filter) || module.getTerm().contains(filter.substring(filter.length() - 1))) {
                        filteredModules.add(module);
                    }
                }
            }
        }else{
            for (Module module : moduleList) {
                List<String> moduleTagsTerm = new ArrayList<String>(module.getTags());
                for(String term: module.getTerm()){
                    moduleTagsTerm.add("Term " + term);
                }
                if(new HashSet<>(moduleTagsTerm).containsAll(filters)){
                    filteredModules.add(module);
                }
            }
        }
        list.setAdapter(new ModuleAdapter(filteredModules, listener));
    }

    private void checkForFilter() {
        if (selectedFilters.size() == 1) {
            if (currentSearchText.isEmpty()) {
                list.setAdapter(new ModuleAdapter(moduleList, listener));
            } else {
                ArrayList<Module> filteredModules = new ArrayList<Module>();
                for (Module module : moduleList) {
                    if (module.getName().toLowerCase().contains(currentSearchText.toLowerCase())) {
                        filteredModules.add(module);
                    }
                }
                list.setAdapter(new ModuleAdapter(filteredModules, listener));
            }
        } else {
            applyFilter();
        }
    }

    private void showFilterDialog() {
        Dialog filterDialog = new Dialog(this.getContext());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filterlayout);

        setUpChips(filterDialog);
        for (FilterChip chipItem : filterChips) {
            Chip chip = chipItem.getChip();
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "click");
                    if (chip.isChecked()) {
                        chipItem.setChipCheck(true);
                        addFilter(chipItem.getName());
                    } else {
                        chipItem.setChipCheck(false);
                        removeFilter(chipItem.getName());
                    }
                }
            });
        }

        Button applyButton = filterDialog.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForFilter();
                filterDialog.dismiss();
            }
        });

        Button resetButton = filterDialog.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilters.clear();
                selectedFilters.add("all");
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

    public void setUpChips(Dialog filterDialog) {
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
        Collections.sort(moduleList,Module.idAscending);
    }

//    public void sortChips(View view){
//        Chip idChip = view.findViewById(R.id.id);
//        Chip nameChip = view.findViewById(R.id.name);
//        Chip ascChip = view.findViewById(R.id.ascending);
//        Chip descChip = view.findViewById(R.id.descending);
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
//    }
}
//});
//
//idAscButton = view.findViewById(R.id.idAsc);
//        idAscButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Collections.sort(moduleList,Module.idAscending);
//        checkForFilter();
//    }
//});
//idDescButton = view.findViewById(R.id.idDesc);
//        idDescButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Collections.sort(moduleList, Module.idAscending);
//        Collections.reverse(moduleList);
//        checkForFilter();
//    }
//});
//nameAscButton = view.findViewById(R.id.nameAsc);
//        nameAscButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Collections.sort(moduleList, Module.nameAscending);
//        checkForFilter();
//    }
//});
//nameDescButton = view.findViewById(R.id.nameDesc);
//        nameDescButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Collections.sort(moduleList, Module.nameAscending);
//        Collections.reverse(moduleList);
//        checkForFilter();
//    }
//});