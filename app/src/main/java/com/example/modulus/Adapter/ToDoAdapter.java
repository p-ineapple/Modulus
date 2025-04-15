package com.example.modulus.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ToDoModel;
import com.example.modulus.FragmentHome.HomeFragment;
import com.example.modulus.FragmentHome.AddNewTask;
import com.example.modulus.R;
import com.example.modulus.FragmentHome.DataBaseHelperHome;

import java.util.List;
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> mList;
    private HomeFragment home;
    private DataBaseHelperHome myDB;

    public ToDoAdapter(DataBaseHelperHome myDB, HomeFragment home){
        this.home = home;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_task_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.taskDescription.setText(item.getTask());//ToDo: edit her to add timing i think
        holder.taskCat.setText(item.getCategory());
        holder.taskTime.setText(item.getTime());
        /*
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        Log.d("homeadapter",String.valueOf(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (buttonView.isChecked()){
                    myDB.updateStatus(item.getId(), 1);
                }else{
                    myDB.updateStatus(item.getId(), 0);
                }
            }
        });*/
        // Temporarily remove listener before setting checked state
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(toBoolean(item.getStatus()));

        // Re-attach listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            myDB.updateStatus(item.getId(), newStatus);
            item.setStatus(newStatus); // <- Optional but useful if you need local update
            Log.d("ToDoAdapter", "Updated task id " + item.getId() + " to status " + newStatus);
        });
    }
    public boolean toBoolean(int num){
        return num != 0;
    }
    public Context getContext(){
        return home.getContext();
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItems(int position){
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("category", item.getCategory());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        Log.d("edit item",String.valueOf(item.getId()));
        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(home.getChildFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView taskCat;
        TextView taskDescription;
        TextView taskTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            taskCat = itemView.findViewById(R.id.taskCat);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskTime = itemView.findViewById(R.id.taskTime);
        }

    }
}