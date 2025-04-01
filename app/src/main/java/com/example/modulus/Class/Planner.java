package com.example.modulus.Class;

import java.util.ArrayList;
import java.util.List;

public class Planner {
    private String term;
    private ArrayList<Module> modules = new ArrayList<>();
    public Planner(String term){
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        if(modules.size() == 4 || modules.size() == 5){
            this.modules = modules;
        }
    }
}
