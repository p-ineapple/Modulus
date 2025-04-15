package com.example.modulus.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;

import java.util.List;

public class NestedPlannerAdapter extends RecyclerView.Adapter<NestedPlannerAdapter.NestedModuleViewHolder> {
    private List<ModuleModel> moduleList;

    public NestedPlannerAdapter(List<ModuleModel> moduleList){
        this.moduleList = moduleList;
    }
    @Override
    public NestedModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_cell , parent , false);
        return new NestedModuleViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NestedModuleViewHolder holder, int position) {
        if(moduleList.get(position) != null){
            ModuleModel module = moduleList.get(position);
            if (module.getName() == "Capstone"){
                holder.plannerModule.setText("Capstone");
            } else{
                holder.plannerModule.setText(module.toString());
            }
//        holder.plannerCell.setBackgroundColor(R.color.dark_pink);
//        if(Objects.equals(module.getName(), "Capstone")){
//            holder.plannerCell.setBackgroundColor(R.color.light_pink);
//        }
//        if(module.getTags() != null) {
//            if (module.getTags().contains("HASS")) {
//                holder.plannerCell.setBackgroundColor(R.color.light_blue);
//            }
//        }
        }
    }

    @Override
    public int getItemCount() {
        if(moduleList != null){
            return moduleList.size();
        }
        return 0;
    }
    public class NestedModuleViewHolder extends RecyclerView.ViewHolder{
        TextView plannerModule;
        public NestedModuleViewHolder(View itemView) {
            super(itemView);
            plannerModule = itemView.findViewById(R.id.plannerModule);
        }
    }
}