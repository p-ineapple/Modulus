//package com.example.modulus.Adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.modulus.Class.Module;
//import com.example.modulus.Class.Planner;
//import com.example.modulus.R;
//
//import java.util.List;
//
//public class PlannerAdaptor extends RecyclerView.Adapter<PlannerAdaptor.plannerViewHolder> {
//    public PlannerAdaptor(@NonNull Context context, int resource, @NonNull List<Planner> objects) {
//        super(context, resource, objects);
//    }
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Module module = getItem(position);
//
//        if(convertView == null)
//        {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planner_cell, parent, false);
//        }
//        TextView moduleName = (TextView) convertView.findViewById(R.id.plannerName);
//        moduleName.setText(module.getId() + " - " + module.getName());
//
//        return convertView;
//    }
//
//    static class plannerViewHolder extends RecyclerView.ViewHolder{
//        int holderID;
//        public plannerViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//
//    @NonNull
//    @Override
//    public plannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PlannerAdaptor.plannerViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}