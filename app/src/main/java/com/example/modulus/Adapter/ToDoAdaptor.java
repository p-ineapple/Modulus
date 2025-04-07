package com.example.modulus.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Class.ToDoModel;
import com.example.modulus.Home.HomeFragment;
import com.example.modulus.Home.AddNewTask;
import com.example.modulus.R;
import com.example.modulus.Home.DataBaseHelper;

import java.util.List;
public class ToDoAdaptor extends RecyclerView.Adapter<ToDoAdaptor.MyViewHolder> {
    private List<ToDoModel> mList;
    private HomeFragment home;
    private DataBaseHelper myDB;

    public ToDoAdaptor(DataBaseHelper myDB, HomeFragment home){
        this.home = home;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.checkBox.setText(item.getTask());//ToDo: edit her to add timing i think
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (buttonView.isChecked()){
                    myDB.updateStatus(item.getId(), 1); // tutorial said it put int 1 part 3, 29:27
                }else{
                    myDB.updateStatus(item.getId(), 0);
                }
            }
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
        Log.d("edit item",String.valueOf(item.getId()));
        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

    }
}