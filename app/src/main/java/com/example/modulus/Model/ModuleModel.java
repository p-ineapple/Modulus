package com.example.modulus.Model;

import android.graphics.drawable.Drawable;

import java.util.Comparator;
import java.util.List;

public class ModuleModel {
    private String id;
    private String name;
    private String pillar;
    private List<String> tags;
    private List<String> term;
    private List<String> prof;
    private List<String> prerequisites;
    private String cost;
    private String description;
    private Drawable image;
    private int color;
    public ModuleModel(){
    }
    public ModuleModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        if(id == null){
            return "";
        }else{
            return id;
        }
    }
    public void setId(String id) {
        this.id = id;
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
    public List<String> getTags() {
        if(tags == null) {
            return null;
        }else{
            return tags;
        }
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTerm() {
        if(term == null) {
            return null;
        }else{
            return term;
        }
    }
    public void setTerm(List<String> term) {
        this.term = term;
    }
    public List<String> getProf() {
        if(prof == null) {
            return null;
        }else{
            return prof;
        }
    }

    public void setProf(List<String> prof) {
        this.prof = prof;
    }
    public List<String> getPrerequisites() {
        return prerequisites;
    }
    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString(){
        return getId() + " - " + getName();
    }
    public static ModuleModel getModuleFromString(String s){
        if(s.contains(" - ")){
            String[] module = s.split(" - ");
            return new ModuleModel(module[0], module[1]);
        }return null;
    }
    public static Comparator<ModuleModel> idCompare = new Comparator<ModuleModel>() {
        @Override
        public int compare(ModuleModel mod1, ModuleModel mod2) {
            String id1 = mod1.getId();
            String id2 = mod2.getId();
            return id1.compareTo(id2);
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
    public static Comparator<ModuleModel> nameCompare = new Comparator<ModuleModel>() {
        @Override
        public int compare(ModuleModel mod1, ModuleModel mod2) {
            String name1 = mod1.getName();
            String name2 = mod2.getName();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();
            return name1.compareTo(name2);
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
