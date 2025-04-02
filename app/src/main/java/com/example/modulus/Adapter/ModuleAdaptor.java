package com.example.modulus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Class.Module;
import com.example.modulus.R;

import java.util.ArrayList;

public class ModuleAdaptor extends RecyclerView.Adapter<ModuleAdaptor.moduleCellViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Module module);
    }

    ArrayList<Module> moduleList;
    OnItemClickListener listener;
    public ModuleAdaptor(ArrayList<Module> moduleList, OnItemClickListener listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    static class moduleCellViewHolder extends RecyclerView.ViewHolder{
        TextView moduleName;
        TextView moduleTermProf;
        TextView moduleTags;
        public moduleCellViewHolder(View itemView){
            super(itemView);
            moduleName = (TextView) itemView.findViewById(R.id.moduleName);
            moduleTermProf = (TextView) itemView.findViewById(R.id.moduleTermProf);
            moduleTags = (TextView) itemView.findViewById(R.id.moduleTags);
        }

        public void bind(Module item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public moduleCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.module_cell, parent, false);
        return new moduleCellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull moduleCellViewHolder holder, int position) {
        Module module = moduleList.get(position);
        holder.moduleName.setText(module.getId() + " - " + module.getName());
        holder.moduleTermProf.setText("Term(s): " + String.join(", ", module.getTerm())
                + " | " + String.join(", ", module.getProf()));
        holder.moduleTags.setText(String.join(", ", module.getTags()));
//        for(String tag : module.getTags()) {
//            Chip chip = new Chip(holder.itemView.getContext());
//            chip.setText(tag);
//            chip.setEnsureMinTouchTargetSize(false);
//            chip.setTextStartPadding(0);
//            chip.setTextEndPadding(0);
//            holder.moduleTags.addView(chip);
//        }
        holder.bind(module, listener);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

}