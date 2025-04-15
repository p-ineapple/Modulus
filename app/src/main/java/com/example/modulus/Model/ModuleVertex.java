package com.example.modulus.Model;

import java.util.List;

public class ModuleVertex {
    String type;
    boolean preRequisite;
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
        if(!preReq.equals("NIL")){
            preRequisite = true;
            if(module.getCost().equals("Hard")){
                this.hard = preReq;
            }else{
                this.soft = preReq;
                for(String preReqMod: preReq){
                    if(preReqMod.contains("/")){
                        this.soft_count = 1;
                    }else{
                        this.soft_count = 0;
                    }
                }
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
