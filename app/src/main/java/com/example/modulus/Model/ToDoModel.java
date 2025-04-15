package com.example.modulus.Model;

import java.util.Comparator;

public class ToDoModel {
    private String task;
    private String date;
    private String time;
    private String category;
    private int id, status;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}
    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public static Comparator<ToDoModel> timeCompare = new Comparator<ToDoModel>() {
        @Override
        public int compare(ToDoModel model1, ToDoModel model2) {
            String time1 = model1.getTime();
            String time2 = model2.getTime();

            return time1.compareTo(time2);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
    public static Comparator<ToDoModel> statusCompare = new Comparator<ToDoModel>() {
        @Override
        public int compare(ToDoModel model1, ToDoModel model2) {
            int status1 = model1.getStatus();
            int status2 = model2.getStatus();
            if (status1 > status2){
                return 1;
            }else if(status1 < status2){
                return -1;
            }else{
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
}
