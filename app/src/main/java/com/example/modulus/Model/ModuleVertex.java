package com.example.modulus.Model;

import java.util.List;

public class ModuleVertex {
    String type;
    List<String> hard = null;
    List<String> soft = null;
    int soft_count = 0;
    public ModuleVertex(ModuleModel module){
        if(module.getTags().contains("HASS")){
            this.type = "HASS";
        }else{
            this.type = "Pillar";
        }
        List<String> preReq = module.getPrerequisites();
        if(!preReq.isEmpty()){
            if(preReq.contains("OR")){
                this.soft = preReq;
            }else{
                this.hard = preReq;
            }
        }
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
