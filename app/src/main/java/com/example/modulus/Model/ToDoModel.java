package com.example.modulus.Model;

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
}
