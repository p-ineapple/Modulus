package com.example.modulus.Insights;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.modulus.Class.Module;
import com.example.modulus.Adapter.ModuleAdaptor;
import com.example.modulus.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;

//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;

public class InsightsFragment extends Fragment{
    static ArrayList<Module> moduleList = new ArrayList<Module>();
    ArrayList<String> selectedFilters = new ArrayList<String>();
    String currentSearchText = "";
    ListView list;
    SearchView search;
    ImageButton filterButton;
    Chip asdChip, esdChip, epdChip, daiChip, istdChip, hassChip, smtChip,
            term1Chip, term2Chip, term3Chip, term4Chip, term5Chip, term6Chip, term7Chip, term8Chip,
    coreChip, coreEChip, electiveChip, fCoreChip, fElectiveChip;
    boolean asdChipCheck, esdChipCheck, epdChipCheck, daiChipCheck, istdChipCheck, hassChipCheck, smtChipCheck,
            term1ChipCheck, term2ChipCheck, term3ChipCheck, term4ChipCheck, term5ChipCheck, term6ChipCheck, term7ChipCheck, term8ChipCheck,
    coreChipCheck, coreEChipCheck, electiveChipCheck, fCoreChipCheck, fElectiveChipCheck;
//    boolean sortHidden = true; boolean filterHidden = true; LinearLayout sortView; Button sortButton;
//    Button allButton, idAscButton, idDescButton, nameAscButton, nameDescButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_insights, container, false);
        //set up modules list
        if(moduleList.isEmpty()){
            setupData();
        }
        list = view.findViewById(R.id.modulesListView);
        ModuleAdaptor adapter = new ModuleAdaptor(getContext(), 0, moduleList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Module selectModule = (Module) (list.getItemAtPosition(position));
                Intent showDetail = new Intent(getContext(), ModuleDetailsActivity.class);
                showDetail.putExtra("id", selectModule.getId());
                startActivity(showDetail);
            }
        });

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
                for(Module module: moduleList) {
                    if(module.getName().toLowerCase().contains(s.toLowerCase()) || module.getId().contains(s)) {
                        if(selectedFilters.size() == 1) {
                            filteredModules.add(module);
                        } else {
                            for(String filter: selectedFilters) {
                                if (module.getName().toLowerCase().contains(filter) || module.getId().contains(s)) {
                                    filteredModules.add(module);
                                }
                            }
                        }
                    }
                }
                list.setAdapter(new ModuleAdaptor(getContext(), 0, filteredModules));
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
        return view;
    }
    private void setupData() {
        Module HASS = new Module("0", "HASS Test");
        HASS.setTags(Arrays.asList("HASS", "Elective / Technical Elective"));
        HASS.setProf(Arrays.asList("HASS Prof"));
        HASS.setTerm(Arrays.asList("4", "6", "8"));
        HASS.setPrerequisites(Arrays.asList("NIL"));
        moduleList.add(HASS);

        Module DAI = new Module("1", "DAI Test");
        DAI.setTags(Arrays.asList("DAI", "Core"));
        DAI.setProf(Arrays.asList("DAI Prof"));
        DAI.setTerm(Arrays.asList("7"));
        DAI.setPrerequisites(Arrays.asList("NIL"));
        moduleList.add(DAI);
    }
    private void addFilter(String status) {
        if(status != null && !selectedFilters.contains(status))
            selectedFilters.add(status);
    }
//    private void removeFilter(String status) {
//        selectedFilters.remove(status);
//        applyFilter();
//    }
    private void applyFilter() {
        ArrayList<Module> filteredModules = new ArrayList<Module>();
        for(Module module : moduleList) {
            for(String filter: selectedFilters) {
                if(module.getTags().contains(filter) || module.getTerm().contains(filter)) {
                    filteredModules.add(module);
                    }
                }
            }
        list.setAdapter(new ModuleAdaptor(getContext(), 0, filteredModules));
    }

    private void checkForFilter() {
        if(selectedFilters.size()==1) {
            if(currentSearchText.isEmpty()) {
                list.setAdapter(new ModuleAdaptor(getContext(), 0, moduleList));
            }
            else {
                ArrayList<Module> filteredModules = new ArrayList<Module>();
                for(Module module: moduleList) {
                    if(module.getName().toLowerCase().contains(currentSearchText.toLowerCase())) {
                        filteredModules.add(module);
                    }
                }
                list.setAdapter(new ModuleAdaptor(getContext(), 0, filteredModules));
            }
        } else {
            applyFilter();
        }
    }

    private void showFilterDialog() {
        Dialog filterDialog = new Dialog(this.getContext());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filterlayout);

        ImageView closeButton = filterDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        ArrayList<Chip> chipArrayList = new ArrayList<Chip>();

        asdChip = filterDialog.findViewById(R.id.asdFilter); chipArrayList.add(asdChip); asdChip.setChecked(asdChipCheck);
        asdChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asdChip.isChecked()) {
                    asdChipCheck = true;
                    addFilter("ASD");
                } else {
                    asdChipCheck = false;
                    selectedFilters.remove("ASD");
                }
            }
        });
        istdChip = filterDialog.findViewById(R.id.istdFilter); chipArrayList.add(istdChip); istdChip.setChecked(istdChipCheck);
        istdChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (istdChip.isChecked()) {
                    istdChipCheck = true;
                    addFilter("ISTD");
                } else {
                    istdChipCheck = false;
                    selectedFilters.remove("ISTD");
                }
            }
        });
        esdChip = filterDialog.findViewById(R.id.esdFilter); chipArrayList.add(esdChip); esdChip.setChecked(esdChipCheck);
        esdChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esdChip.isChecked()) {
                    esdChipCheck = true;
                    addFilter("ESD");
                } else {
                    esdChipCheck = false;
                    selectedFilters.remove("ESD");
                }
            }
        });
        epdChip = filterDialog.findViewById(R.id.epdFilter); chipArrayList.add(epdChip); epdChip.setChecked(epdChipCheck);
        epdChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (epdChip.isChecked()) {
                    epdChipCheck = true;
                    addFilter("EPD");
                } else {
                    epdChipCheck = false;
                    selectedFilters.remove("EPD");
                }
            }
        });
        daiChip = filterDialog.findViewById(R.id.daiFilter); chipArrayList.add(daiChip); daiChip.setChecked(daiChipCheck);
        daiChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daiChip.isChecked()) {
                    daiChipCheck = true;
                    addFilter("DAI");
                } else {
                    daiChipCheck = false;
                    selectedFilters.remove("DAI");
                }
            }
        });
        hassChip = filterDialog.findViewById(R.id.hassFilter); chipArrayList.add(hassChip); hassChip.setChecked(hassChipCheck);
        hassChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hassChip.isChecked()) {
                    hassChipCheck = true;
                    addFilter("HASS");
                } else {
                    hassChipCheck = false;
                    selectedFilters.remove("HASS");
                }
            }
        });
        smtChip = filterDialog.findViewById(R.id.smtFilter); chipArrayList.add(smtChip); smtChip.setChecked(smtChipCheck);
        smtChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smtChip.isChecked()) {
                    smtChipCheck = true;
                    addFilter("SMT");
                } else {
                    smtChipCheck = false;
                    selectedFilters.remove("SMT");
                }
            }
        });

        term1Chip = filterDialog.findViewById(R.id.term1Filter); chipArrayList.add(term1Chip); term1Chip.setChecked(term1ChipCheck);
        term1Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term1Chip.isChecked()) {
                    term1ChipCheck = true;
                    addFilter("1");
                } else {
                    term3ChipCheck = false;
                    selectedFilters.remove("1");
                }
            }
        });
        term2Chip = filterDialog.findViewById(R.id.term2Filter); chipArrayList.add(term2Chip); term2Chip.setChecked(term2ChipCheck);
        term2Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term2Chip.isChecked()) {
                    term2ChipCheck = true;
                    addFilter("2");
                } else {
                    term2ChipCheck = false;
                    selectedFilters.remove("2");
                }
            }
        });
        term3Chip = filterDialog.findViewById(R.id.term3Filter); chipArrayList.add(term3Chip); term3Chip.setChecked(term3ChipCheck);
        term3Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term3Chip.isChecked()) {
                    term3ChipCheck = true;
                    addFilter("3");
                } else {
                    term3ChipCheck = false;
                    selectedFilters.remove("3");
                }
            }
        });
        term4Chip = filterDialog.findViewById(R.id.term4Filter); chipArrayList.add(term4Chip); term4Chip.setChecked(term4ChipCheck);
        term4Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term4Chip.isChecked()) {
                    term4ChipCheck = true;
                    addFilter("4");
                } else {
                    term4ChipCheck = false;
                    selectedFilters.remove("4");
                }
            }
        });
        term5Chip = filterDialog.findViewById(R.id.term5Filter); chipArrayList.add(term5Chip); term5Chip.setChecked(term5ChipCheck);
        term5Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term5Chip.isChecked()) {
                    term5ChipCheck = true;
                    addFilter("5");
                } else {
                    term5ChipCheck = false;
                    selectedFilters.remove("5");
                }
            }
        });
        term6Chip = filterDialog.findViewById(R.id.term6Filter); chipArrayList.add(term6Chip); term6Chip.setChecked(term6ChipCheck);
        term6Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term6Chip.isChecked()) {
                    term6ChipCheck = true;
                    addFilter("6");
                } else {
                    term6ChipCheck = false;
                    selectedFilters.remove("6");
                }
            }
        });
        term7Chip = filterDialog.findViewById(R.id.term7Filter); chipArrayList.add(term7Chip); term7Chip.setChecked(term6ChipCheck);
        term7Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term7Chip.isChecked()) {
                    term7ChipCheck = true;
                    addFilter("7");
                } else {
                    term7ChipCheck = false;
                    selectedFilters.remove("7");
                }
            }
        });
        term8Chip = filterDialog.findViewById(R.id.term8Filter); chipArrayList.add(term8Chip); term8Chip.setChecked(term8ChipCheck);
        term8Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smtChip.isChecked()) {
                    term8ChipCheck = true;
                    addFilter("8");
                } else {
                    term8ChipCheck = false;
                    selectedFilters.remove("8");
                }
            }
        });

        coreChip = filterDialog.findViewById(R.id.coreFilter); chipArrayList.add(coreChip); coreChip.setChecked(coreChipCheck);
        coreChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coreChip.isChecked()) {
                    coreChipCheck = true;
                    addFilter("Core");
                } else {
                    coreChipCheck = false;
                    selectedFilters.remove("Core");
                }
            }
        });
        coreEChip = filterDialog.findViewById(R.id.coreElectiveFilter); chipArrayList.add(coreEChip); coreEChip.setChecked(coreEChipCheck);
        coreEChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coreEChip.isChecked()) {
                    coreEChipCheck = true;
                    addFilter("Core Elective");
                } else {
                    coreEChipCheck = false;
                    selectedFilters.remove("Core Elective");
                }
            }
        });
        electiveChip = filterDialog.findViewById(R.id.electiveFilter); chipArrayList.add(electiveChip); electiveChip.setChecked(electiveChipCheck);
        electiveChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (electiveChip.isChecked()) {
                    electiveChipCheck = true;
                    addFilter("Elective / Technical Elective");
                } else {
                    electiveChipCheck = false;
                    selectedFilters.remove("Elective / Technical Elective");
                }
            }
        });
        fCoreChip = filterDialog.findViewById(R.id.fCoreFilter); chipArrayList.add(fCoreChip); fCoreChip.setChecked(fCoreChipCheck);
        fCoreChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fCoreChip.isChecked()) {
                    fCoreChipCheck = true;
                    addFilter("Freshmore Core");
                } else {
                    fCoreChipCheck = false;
                    selectedFilters.remove("Freshmore Core");
                }
            }
        });
        fElectiveChip = filterDialog.findViewById(R.id.fElectiveFilter); chipArrayList.add(fElectiveChip); fElectiveChip.setChecked(fElectiveChipCheck);
        fElectiveChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fElectiveChip.isChecked()) {
                    fElectiveChipCheck = true;
                    addFilter("Freshmore Elective");
                } else {
                    fElectiveChipCheck = false;
                    selectedFilters.remove("Freshmore Elective");
                }
            }
        });

        Button applyButton = filterDialog.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForFilter();
            }
        });

        Button resetButton = filterDialog.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilters.clear();
                selectedFilters.add("all");
                for(Chip chip: chipArrayList){
                    chip.setChecked(false);
                }
            }
        });

        filterDialog.show();
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //filterDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        filterDialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}

//original onClick for filter buttons
//@Override
//public void onClick(View v) {
//    if (hassChip.isChecked()) {
//        filterList("HASS");
//    } else {
//        removeFilterList("HASS");
//        checkForFilter();
//    }
//}

//original filter methods
//private void filterList(String status) {
//    if(status != null && !selectedFilters.contains(status))
//        selectedFilters.add(status);
//    applyFilter();
//}
//private void removeFilterList(String status) {
//    selectedFilters.remove(status);
//    applyFilter();
//}

//sort methods
//sortButton = view.findViewById(R.id.sortButton);
//        sortButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if(sortHidden) {
//            sortHidden = false;
//            showSort();
//        } else {
//            sortHidden = true;
//            hideSort();
//        }
//    }
//});
//
//sortView = view.findViewById(R.id.sortTabsLayout2);
//
//allButton = view.findViewById(R.id.allFilter);
//        allButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        selectedFilters.clear();
//        selectedFilters.add("all");
//
//        list.setAdapter(new ModuleAdaptor(getContext(), 0, moduleList));
//    }
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
//
//private void hideSort() {
//    sortView.setVisibility(View.GONE);
//    sortButton.setText("SORT");
//}
//private void showSort() {
//    sortView.setVisibility(View.VISIBLE);
//    sortButton.setText("HIDE");
//}
