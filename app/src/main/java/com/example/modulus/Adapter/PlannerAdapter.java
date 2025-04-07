package com.example.modulus.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Class.Module;
import com.example.modulus.Class.Planner;
import com.example.modulus.R;

import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.plannerViewHolder> {
    List<Planner> plannerList;
    List<Module> moduleList;
    public PlannerAdapter(List<Planner> plannerList) {
        this.plannerList = plannerList;
    }

    @NonNull
    @Override
    public plannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_header, parent, false);
        return new plannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull plannerViewHolder holder, int position) {
        Planner planner = plannerList.get(position);
        moduleList = planner.getModules();
        holder.term.setText(planner.getTerm());

        NestedModuleAdapter adapter = new NestedModuleAdapter(moduleList);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "CLICKKKKK");
                planner.setExpandable(!planner.isExpandable());
                if (planner.isExpandable()){
                    holder.arrow.setImageResource(R.drawable.arrow_up);
                    holder.expandableLayout.setVisibility(View.VISIBLE);

                }else{
                    holder.arrow.setImageResource(R.drawable.arrow_down);
                    holder.expandableLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plannerList.size();
    }

    static class plannerViewHolder extends RecyclerView.ViewHolder {
        TextView term;
        ImageView arrow;
        LinearLayout layout;
        RecyclerView nestedRecyclerView;
        LinearLayout expandableLayout;

        public plannerViewHolder(View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.term);
            arrow = itemView.findViewById(R.id.arrow);
            layout = itemView.findViewById(R.id.plannerTerm);
            nestedRecyclerView = itemView.findViewById(R.id.plannerNestedRecyclerView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
        }
    }
}
