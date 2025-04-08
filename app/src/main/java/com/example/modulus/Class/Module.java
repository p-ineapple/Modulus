package com.example.modulus.Class;

import java.util.Comparator;
import java.util.List;

public class Module {
    private String id;
    private String name;
    private List<String> prof;
    private List<String> tags;
    private List<String> term;
    private List<String> prerequisites;
    private String description;
    public Module(){
    }
    public Module(String id, String name) {
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
    public List<String> getPrerequisites() {
        return prerequisites;
    }
    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
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
    public static Module getModuleFromString(String s){
        if(s.contains(" - ")){
            String[] module = s.split(" - ");
            return new Module(module[0], module[1]);
        }else if(s.contains("Capstone")){
            return new Module("", "Capstone");
        }else{
            return null;
        }
    }

    public static Comparator<Module> idAscending = new Comparator<Module>() {
        @Override
        public int compare(Module mod1, Module mod2) {
            String id1 = mod1.getId();
            String id2 = mod2.getId();

            return id1.compareTo(id2);
        }
    };

    public static Comparator<Module> nameAscending = new Comparator<Module>() {
        @Override
        public int compare(Module mod1, Module mod2){
            String name1 = mod1.getName();
            String name2 = mod2.getName();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();
            return name1.compareTo(name2);
        }
    };


}
