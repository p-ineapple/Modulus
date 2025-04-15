//package com.example.modulus.Model;
//
//import com.example.modulus.FragmentInsights.InsightsFragment;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//public class Kahn {
//    List<ModuleVertex> adjList;
//    List<ModuleVertex> requiredList;
//    public void setUpGraph() {
//        for (ModuleModel mod : InsightsFragment.moduleList) {
//            adjList.add(new ModuleVertex(mod));
//        }
//
//    }
//
//    public List<ModuleVertex> RestrictedSet(TrackModel trackModel) {
//        ArrayList<ModuleModel> required = new ArrayList<>();
//        required.addAll(trackModel.getCore());
//        required.addAll(trackModel.getElectives());
////        if (minorModel != null) {
////            required.addAll(minorModel.getCore());
////            required.addAll(minorModel.getElectives());
////        }
//        ArrayList<ModuleVertex> requiredGraph = new ArrayList<>();
//        for(ModuleModel m: required){
//            requiredGraph.add(new ModuleVertex(m));
//        }
//        for(ModuleVertex v: adjList){
//            if(isPreReqRequired(v, requiredGraph)){
//                requiredGraph.add(v);
//            }
//        }
//        requiredList = requiredGraph;
//        return requiredGraph;
//    }
//
//    public boolean isPreReqRequired(ModuleVertex moduleModel, List<ModuleVertex> list) {
//        for (ModuleVertex u: list){
//            List<ModuleVertex> preReq = u.hard;
//            for(ModuleVertex m: preReq){
//                if(m.getId().equals(u.getId())){
//                    return true;
//                }
//            }
//        }return false;
//    }
//
//    public List<List<ModuleModel>> plan(){
//        List<List<ModuleModel>> all = new ArrayList<>();
//        List<PlannerModel> schedule = new ArrayList<>();
//        boolean HASSFlag = false;
//        int currentTerm = 0;
//        List<ModuleModel> order = new ArrayList<>();
//        //inDeg
//        permute();
//
//    }
//
//    public void permute(TrackModel trackModel, List<List<ModuleModel>> all, List<PlannerModel> schedule, boolean hassFlag, List<ModuleModel> order, int currentTerm){
//        if(currentTerm > 4){
//            if(new HashSet<>(order).containsAll(trackModel.getCore())){
//                all.add(order);
//                return;
//            }
//        }
//        List<ModuleModel> tempPlannerMods = new ArrayList<>();
//        schedule.get(currentTerm).setModules(tempPlannerMods);
//        fill(trackModel, all, schedule, hassFlag, order, currentTerm, 0);
//    }
//
//    public void fill(TrackModel trackModel, List<List<ModuleModel>> all, List<PlannerModel> schedule, boolean hassFlag, List<ModuleModel> order, int currentTerm, int count){
//        if(count == 4){
//            boolean hasHASS = false;
//            for(ModuleModel m: schedule.get(currentTerm).getModules()){
//                if(m.getTags().contains("HASS")){
//                    hasHASS = true;
//                }
//            }
//            order.addAll(schedule.get(currentTerm).getModules());
//            permute(trackModel, all, schedule, hasHASS, order, currentTerm);
//        }
//        for(ModuleVertex v: requiredList){
//            if(!order.toString().contains(v.toString())){
//
//            }
//        }
//    }
//
//    public boolean preReqCheck(){}
//}