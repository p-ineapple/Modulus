package com.example.modulus.FragmentInsights;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Adapter.ModuleAdapter;
import com.example.modulus.Model.FilterChipModel;
import com.example.modulus.Utils.MergeSort;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.zhanghai.android.fastscroll.FastScroller;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class InsightsFragment extends Fragment {
    public static ArrayList<ModuleModel> moduleList;
    private ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";
    private RecyclerView modulesRecyclerView;
    private ModuleAdapter.OnItemClickListener listener;
    private ConstraintLayout sortTab;
    private final MergeSort sortID = new MergeSort(ModuleModel.idCompare), sortName = new MergeSort(ModuleModel.nameCompare);
    private final ArrayList<FilterChipModel> filterChips = new ArrayList<FilterChipModel>();

    private String currentSortType = "name";
    final String TAG = "Insights Fragment";

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        // Set up modules modulesRecyclerView
        if (moduleList == null) {
            Log.d(TAG, "Setting up modules");
            DataBaseHelperInsights myDB = new DataBaseHelperInsights(getContext());
            moduleList = myDB.getAllModules();
        }
        Log.d(TAG, "Database set up");

        modulesRecyclerView = view.findViewById(R.id.recyclerView);
        listener = new ModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ModuleModel module) {
                Intent showDetail = new Intent(getContext(), ModuleDetailsActivity.class);
                showDetail.putExtra("id", module.getId());
                startActivity(showDetail);
            }
        };
        modulesRecyclerView.setAdapter(new ModuleAdapter(moduleList, listener));
        modulesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        FastScroller fastScroller = new FastScrollerBuilder(modulesRecyclerView).useMd2Style().build();

        // Set up searchWidget
        SearchView search = view.findViewById(R.id.modulesListSearchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentSearchText = s;
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener));
                return false;
            }
        });

        // Set up filter
        ImageButton filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        int searchPlateId = search.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = search.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundResource(R.color.white);
        }

        // Set up sort chips
        sortTab = view.findViewById(R.id.sortTab);
        sortTab.setVisibility(View.VISIBLE);
        ImageButton sortButton = view.findViewById(R.id.sortButton);
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
                currentSortType = "subjectCode";
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && asc.isChecked()) {
                    sortID.mergeSort(filteredModules, filteredModules.size());
                } else if (id.isChecked() && des.isChecked()) {
                    sortID.mergeSort(filteredModules, filteredModules.size());
                    Collections.reverse(filteredModules);
                }
                ModuleAdapter adapter = new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener);
                adapter.setSortType(currentSortType);
                modulesRecyclerView.setAdapter(adapter);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSortType = "name";
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (name.isChecked() && asc.isChecked()) {
                    sortName.mergeSort(filteredModules, filteredModules.size());
                } else if (name.isChecked() && des.isChecked()) {
                    sortName.mergeSort(filteredModules, filteredModules.size());
                    Collections.reverse(filteredModules);
                }
                ModuleAdapter adapter = new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener);
                adapter.setSortType(currentSortType);
                modulesRecyclerView.setAdapter(adapter);
            }
        });
        asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && asc.isChecked()) {
                    sortID.mergeSort(filteredModules, filteredModules.size());
                } else if (name.isChecked() && asc.isChecked()) {
                    sortName.mergeSort(filteredModules, filteredModules.size());
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener));
            }
        });
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                if (id.isChecked() && des.isChecked()) {
                    sortID.mergeSort(filteredModules, filteredModules.size());
                    Collections.reverse(filteredModules);
                } else if (name.isChecked() && des.isChecked()) {
                    sortName.mergeSort(filteredModules, filteredModules.size());
                    Collections.reverse(filteredModules);
                }
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener));
            }
        });
        return view;
    }

    private void showFilterDialog() {
        Dialog filterDialog = new Dialog(this.getContext());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.insights_filter_layout);

        setUpFilterChips(filterDialog);
        for (FilterChipModel chipItem : filterChips) {
            Chip chip = chipItem.getChip();
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, chip.getText() + "checked");
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
                List<ModuleModel> filteredModules = Filter.checkForFilter(moduleList, selectedFilters, currentSearchText);
                modulesRecyclerView.setAdapter(new ModuleAdapter((ArrayList<ModuleModel>) filteredModules, listener));
                filterDialog.dismiss();
            }
        });

        Button resetButton = filterDialog.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilters.clear();
                for (FilterChipModel chip : filterChips) {
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
    public void setUpFilterChips(Dialog filterDialog) {
        List<String> filterChipNames = Arrays.asList(("ASD,EPD,ESD,DAI,ISTD,HASS,SMT," +
                "Term 1,Term 2,Term 3,Term 4,Term 5,Term 6,Term 7,Term 8," +
                "Core,Core Elective,Freshmore Core,Freshmore Elective,Elective / Technical Elective").split(","));
        ChipGroup pillarChips = filterDialog.findViewById(R.id.pillarChips);
        ChipGroup termChips = filterDialog.findViewById(R.id.termChips);
        ChipGroup courseChips = filterDialog.findViewById(R.id.courseChips);
        if (filterChips.isEmpty()) {
            for (int i = 0; i < filterChipNames.size(); i++) {
                FilterChipModel filterChip = new FilterChipModel(filterChipNames.get(i), false);
                filterChips.add(filterChip);
            }
        }
        for (int i = 0; i < filterChipNames.size(); i++) {
            Chip chip = new Chip(this.getContext());
            chip.setChipDrawable(ChipDrawable.createFromAttributes(this.getContext(), null, 0, R.style.Chip2));
            chip.setText(filterChipNames.get(i));
            chip.setCheckable(true);
            chip.setChecked(filterChips.get(i).isChipCheck());


            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_purple)));
                    chip.setTextColor(getResources().getColor(R.color.white));
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.light_purple)));
                    chip.setTextColor(getResources().getColor(R.color.black));
                }
            });

            if (i < 7) {
                pillarChips.addView(chip);
            } else if (i < 15) {
                termChips.addView(chip);
            } else {
                courseChips.addView(chip);
            }
            filterChips.get(i).setChip(chip);
        }
        sortID.mergeSort(moduleList, moduleList.size());
    }

    @NonNull
    protected FastScroller createFastScroller(@NonNull RecyclerView recyclerView) {
        return new FastScrollerBuilder(recyclerView).build();
    }
}