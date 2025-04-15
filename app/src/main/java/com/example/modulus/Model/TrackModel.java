package com.example.modulus.Model;

import java.util.List;

public class TrackModel {
    private String name;
    private String pillar;
    private List<ModuleModel> core;
    private List<ModuleModel> recMods = null;
    private List<ModuleModel> electives;
    public TrackModel(String name, String pillar){
        this.name = name;
        this.pillar = pillar;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPillar() {
        return pillar;
    }
    public void setPillar(String pillar) {
        this.pillar = pillar;
    }
    public List<ModuleModel> getCore() {
        return core;
    }
    public void setCore(List<ModuleModel> core) {
        this.core = core;
    }
    public List<ModuleModel> getRecMods() {
        return recMods;
    }
    public void setRecMods(List<ModuleModel> recMods) {
        this.recMods = recMods;
    }
    public List<ModuleModel> getElectives() {
        return electives;
    }
    public void setElectives(List<ModuleModel> electives) {
        this.electives = electives;
    }

    @Override
    public String toString(){
        return getPillar() + " | " + getName() + getCore() + getRecMods() + getElectives();
    }
}
