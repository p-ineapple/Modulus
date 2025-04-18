package com.example.modulus.Adapter;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ModuleModel;
import com.example.modulus.Model.PlannerModel;
import com.example.modulus.R;

import java.util.List;
// RecyclerView Adapter for terms in PlannerFragment
public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.plannerViewHolder> {
    private final int colour;
    List<PlannerModel> plannerList;
    List<ModuleModel> moduleList;


    public PlannerAdapter(List<PlannerModel> plannerList,int colour) {
        this.plannerList = plannerList;
        this.colour = colour;
    }

    @NonNull
    @Override
    public plannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_header, parent, false);
        return new plannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull plannerViewHolder holder, int position) {
        PlannerModel planner = plannerList.get(position);
        moduleList = planner.getModules();
        holder.term.setText(planner.getTerm());
        int holdercolor = ContextCompat.getColor(holder.itemView.getContext(), colour);
        holder.term.setTextColor(holdercolor);
        holder.cardPlanner.setStrokeColor(holdercolor);
        holder.arrow.setImageTintList(ColorStateList.valueOf(holdercolor));

        NestedPlannerAdapter adapter = new NestedPlannerAdapter(moduleList,holdercolor);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "CLICKKKKK");
                planner.setExpandable(!planner.isExpandable());
                if (planner.isExpandable()){
                    holder.arrow.setImageResource(R.drawable.arrow2_up);
                    holder.expandableLayout.setVisibility(View.VISIBLE);

                }else{
                    holder.arrow.setImageResource(R.drawable.arrow2_down);
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

        com.google.android.material.card.MaterialCardView cardPlanner;

        androidx.cardview.widget.CardView modcard;


        public plannerViewHolder(View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.term);
            arrow = itemView.findViewById(R.id.arrow);
            layout = itemView.findViewById(R.id.plannerTerm);
            nestedRecyclerView = itemView.findViewById(R.id.plannerNestedRecyclerView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            cardPlanner = itemView.findViewById(R.id.termcard);
            modcard = itemView.findViewById(R.id.modcard);
        }
    }
}
