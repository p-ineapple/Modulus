package com.example.modulus.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Class.Module;
import com.example.modulus.R;

import java.util.List;
import java.util.Objects;

public class NestedModuleAdapter extends RecyclerView.Adapter<NestedModuleAdapter.NestedModuleViewHolder> {
    private List<Module> moduleList;

    public NestedModuleAdapter(List<Module> moduleList){
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
            Module module = moduleList.get(position);
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
        return moduleList.size();
    }
    public class NestedModuleViewHolder extends RecyclerView.ViewHolder{
        TextView plannerModule;
        public NestedModuleViewHolder(View itemView) {
            super(itemView);
            plannerModule = itemView.findViewById(R.id.plannerModule);
        }
    }
}