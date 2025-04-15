package com.example.modulus.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.FragmentInsights.ModuleDetailsActivity;
import com.example.modulus.Model.ModuleModel;
import com.example.modulus.R;

import java.util.List;

public class NestedPlannerAdapter extends RecyclerView.Adapter<NestedPlannerAdapter.NestedModuleViewHolder> {
    private List<ModuleModel> moduleList;

    private int holdercolour;
    public NestedPlannerAdapter(List<ModuleModel> moduleList,int holdercolour){
        this.moduleList = moduleList;
        this.holdercolour= holdercolour;
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

            if (holder.modcard != null) {
                holder.modcard.setCardBackgroundColor(holdercolour);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ModuleDetailsActivity.class);
                    intent.putExtra("id", module.getId());
                    view.getContext().startActivity(intent);
                }
            });


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
        androidx.cardview.widget.CardView modcard;


        public NestedModuleViewHolder(View itemView) {
            super(itemView);
            plannerModule = itemView.findViewById(R.id.plannerModule);
            modcard = itemView.findViewById(R.id.modcard);
        }
    }
}