package com.example.modulus.FragmentCalendar;

import com.framgia.library.calendardayview.data.IEvent;

import java.util.Calendar;

public class Event implements IEvent {

    private int Id;
    private Calendar startTime,endTime,eventDate;
    private String name,location;

    private int color;

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getColour() {
        return color;
    }

    public void setColour(int color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isOnDate(Calendar date) {
        return this.eventDate.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                this.eventDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                this.eventDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH);
    }

    //Constructor
    public Event(int Id, Calendar eventDate, Calendar startTime, Calendar endTime, String name, String location, int color){
        this.Id = Id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.location = location;
        this.color = color;
        this.eventDate = eventDate;
    }


}
