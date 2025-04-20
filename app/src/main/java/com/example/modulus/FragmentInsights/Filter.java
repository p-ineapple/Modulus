package com.example.modulus.FragmentInsights;

import com.example.modulus.Model.ModuleModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Filter {
    // Function names define usage
    public void addFilter(List<String> selectedFilters, String status) {
        if (status != null && !selectedFilters.contains(status)) {
            selectedFilters.add(status);
        }
    }

    public void removeFilter(List<String> selectedFilters, String status) {
        if (status != null){
            selectedFilters.remove(status);
        }
    }

    public ArrayList<ModuleModel> applySelectedFilter(List<ModuleModel> moduleList, List<String> selectedFilters) {
        ArrayList<ModuleModel> filteredModules = new ArrayList<ModuleModel>();
        List<String> pillars = Arrays.asList(("ASD,ESD,EPD,DAI,ISTD,HASS,SMT").split(","));
        List<String> terms = Arrays.asList(("Term 1,Term 2,Term 3,Term 4,Term 5,Term 6,Term 7,Term 8").split(","));
        List<String> courses = Arrays.asList(("Core,Core Elective,Elective / Technical Elective,Freshmore Core,Freshmore Elective").split(","));
        if(new HashSet<>(pillars).containsAll(selectedFilters) || new HashSet<>(terms).containsAll(selectedFilters) || new HashSet<>(courses).containsAll(selectedFilters)){
            for (ModuleModel module : moduleList) {
                for (String filter : selectedFilters) {
                    if (module.getTags().contains(filter) || module.getTerm().contains(filter.substring(filter.length() - 1))) {
                        filteredModules.add(module);
                    }
                }
            }
        }else{
            for (ModuleModel module : moduleList) {
                List<String> moduleTagsTerm = new ArrayList<String>(module.getTags());
                for(String term: module.getTerm()){
                    moduleTagsTerm.add("Term " + term);
                }
                if(new HashSet<>(moduleTagsTerm).containsAll(selectedFilters)){
                    filteredModules.add(module);
                }
            }
        }
        return filteredModules;
    }
    public List<ModuleModel> applySearchText(List<ModuleModel> moduleList, String currentSearchText){
        List<ModuleModel> searchModules = new ArrayList<ModuleModel>();
        for (ModuleModel module : moduleList) {
            if (module.getName().toLowerCase().contains(currentSearchText.toLowerCase()) || module.getId().contains(currentSearchText)) {
                searchModules.add(module);
            }
        }
        return searchModules;
    }
    public List<ModuleModel> checkForFilter(List<ModuleModel> moduleList, List<String> selectedFilters, String currentSearchText) {
        if (selectedFilters.isEmpty()) {
            if (currentSearchText.isEmpty()) {
                return moduleList;
            } else {
                return applySearchText(moduleList, currentSearchText);
            }
        } else {
            if (currentSearchText.isEmpty()){
                return applySelectedFilter(moduleList, selectedFilters);
            } else {
                return applySearchText( applySelectedFilter(moduleList, selectedFilters), currentSearchText);
            }
        }
    }
}
