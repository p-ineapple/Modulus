package com.example.modulus.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Class.Module;
import com.example.modulus.Class.Planner;
import com.example.modulus.Planner.EditPlanner;
import com.example.modulus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditPlannerAdapter extends RecyclerView.Adapter<EditPlannerAdapter.EditPlannerViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Planner planner);
    }
    List<Planner> plannerList;
    OnItemClickListener listener;
    public EditPlannerAdapter(List<Planner> plannerList, OnItemClickListener listener) {
        this.plannerList = plannerList;
        this.listener = listener;
    }

    @Override
    public EditPlannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_button, parent, false);
        return new EditPlannerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(EditPlannerAdapter.EditPlannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Planner planner = plannerList.get(position);
        holder.tv.setText(planner.getTerm());
        holder.bind(planner, listener);
    }

    @Override
    public int getItemCount() {
        return plannerList.size();
    }

    public class EditPlannerViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public EditPlannerViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.term);
        }
        public void bind(Planner item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
