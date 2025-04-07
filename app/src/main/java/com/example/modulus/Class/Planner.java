package com.example.modulus.Class;

import java.util.ArrayList;
import java.util.List;

public class Planner {
    private String term;
    private int termInt;
    private List<Module> modules = new ArrayList<Module>();
    boolean expandable;
    public Planner(String term){
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

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
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
