package com.example.modulus.Model;

import java.util.ArrayList;
import java.util.List;

public class PlannerModel {
    private String term;
    private int termInt;
    private List<ModuleModel> modules = new ArrayList<ModuleModel>();
    boolean expandable;
    public PlannerModel(String term){
        this.term = term;
        this.expandable = true;
        this.termInt = Integer.parseInt(term.substring(term.length()-1));
    }

    public String getTerm() {
        return term;
    }
    public int getTermInt() {
        return termInt;
    }
    public void setTermInt(int termInt) {
        this.termInt = termInt;
    }

    public List<ModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ModuleModel> modules) {
        this.modules = modules;
    }
    public boolean isExpandable() {
        return expandable;
    }
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    @Override
    public String toString(){
        return getTerm();
    }

}
