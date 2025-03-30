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
    public Module(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
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
        return prof;
    }

    public void setProf(List<String> prof) {
        this.prof = prof;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTerm() {
        return term;
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

    public static Comparator<Module> idAscending = new Comparator<Module>() {
        @Override
        public int compare(Module mod1, Module mod2) {
            int id1 = Integer.parseInt(mod1.getId());
            int id2 = Integer.parseInt(mod2.getId());

            return Integer.compare(id1, id2);
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
