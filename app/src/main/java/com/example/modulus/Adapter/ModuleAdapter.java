package com.example.modulus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.moduleCellViewHolder>  {


    public interface OnItemClickListener {
        void onItemClick(ModuleModel module);
    }
    ArrayList<ModuleModel> moduleList;
    OnItemClickListener listener;
    private String sortType = "name";

    public ModuleAdapter(ArrayList<ModuleModel> moduleList, OnItemClickListener listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public moduleCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_cell, parent, false);
        return new moduleCellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull moduleCellViewHolder holder, int position) {
        ModuleModel module = moduleList.get(position);
        holder.moduleName.setText(module.toString());
        holder.moduleTermProf.setText("Term(s): " + String.join(", ", module.getTerm())
                + " | " + String.join(", ", module.getProf()));
        holder.moduleTags.setText(String.join(", ", module.getTags()));
        holder.bind(module, listener);
        int color = ContextCompat.getColor(holder.itemView.getContext(), module.getColor());
        holder.card.setStrokeColor(color);
        holder.moduleTermProf.setTextColor(color);
        holder.moduleTags.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    static class moduleCellViewHolder extends RecyclerView.ViewHolder{
        TextView moduleName;
        TextView moduleTermProf;
        TextView moduleTags;
        MaterialCardView card;

        public moduleCellViewHolder(View itemView){
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            moduleTermProf = itemView.findViewById(R.id.moduleTermProf);
            moduleTags = itemView.findViewById(R.id.moduleTags);
            card = itemView.findViewById(R.id.module_cell);
        }

        public void bind(ModuleModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
        notifyDataSetChanged();
    }
}