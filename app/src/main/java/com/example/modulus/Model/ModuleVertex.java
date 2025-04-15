package com.example.modulus.Model;

import com.example.modulus.FragmentInsights.InsightsFragment;

import java.util.ArrayList;
import java.util.List;

public class ModuleVertex extends ModuleModel{
    String type;
    boolean preRequisite;
    List<ModuleVertex> hard = null;
    List<ModuleVertex> soft = null;
    public ModuleVertex(ModuleModel module){
        if(module.getTags().contains("HASS")){
            this.type = "HASS";
        }else{
            this.type = "Pillar";
        }
        List<String> preReq = module.getPrerequisites();
        if(!preReq.equals("NIL")){
            this.preRequisite = true;
            List<ModuleVertex> preReqList = new ArrayList<>();
            if(module.getCost().equals("Hard")){
                for(String mod: preReq){
                    if(mod.contains("/")){
                        String[] split = mod.split("/");
                        for(String mm: split){
                            ModuleModel moduleToAdd = InsightsFragment.moduleList.stream().filter(m -> mm.equals(m.getId())).findFirst().orElse(null);
                            preReqList.add(new ModuleVertex(moduleToAdd));
                        }
                    }else{
                        ModuleModel moduleToAdd = InsightsFragment.moduleList.stream().filter(m -> mod.equals(m.getId())).findFirst().orElse(null);
                        preReqList.add(new ModuleVertex(moduleToAdd));
                    }
                }
                this.hard = preReqList;
            }else{
                for(String mod: preReq){
                    if(mod.contains("/")){
                        String[] split = mod.split("/");
                        for(String mm: split){
                            ModuleModel moduleToAdd = InsightsFragment.moduleList.stream().filter(m -> mm.equals(m.getId())).findFirst().orElse(null);
                            preReqList.add(new ModuleVertex(moduleToAdd));
                        }
                    }else{
                        ModuleModel moduleToAdd = InsightsFragment.moduleList.stream().filter(m -> mod.equals(m.getId())).findFirst().orElse(null);
                        preReqList.add(new ModuleVertex(moduleToAdd));
                    }
                }
                this.soft = preReqList;
            }
        }else{
            preRequisite = false;
        }
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
