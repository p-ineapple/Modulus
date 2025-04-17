package com.example.modulus.FragmentCalendar;


import com.framgia.library.calendardayview.data.IPopup;
import java.util.Calendar;

public class Popup implements IPopup{

    private Calendar eventDate,startTime,endTime;
    private String imageStart,imageEnd,description,quote,title;
    private int Id,color;

    //Check if event occurs on the same day as provided
    public boolean isOnDate(Calendar date) {
        return this.eventDate.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                this.eventDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                this.eventDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH);
    }

    //Constructor
    public Popup(int Id, Calendar eventDate, Calendar startTime, Calendar endTime, String title, String description, int color){
        this.Id = Id;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.color = color;
    }

    //All setters and getters
    public void setId(int id) {
        Id = id;
    }
    public int getId() {
        return Id;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }
    public Calendar getEventDate() {
        return eventDate;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String getDescription() {
        return description;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public int getColor() {
        return color;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
    @Override
    public String getQuote() {
        return quote;
    }

    public void setImageStart(String imageStart) {
        this.imageStart = imageStart;
    }
    @Override
    public String getImageStart() {
        return imageStart;
    }

    public void setImageEnd(String imageEnd) {
        this.imageEnd = imageEnd;
    }
    @Override
    public String getImageEnd() {
        return imageEnd;
    }

    @Override
    public Boolean isAutohide() {
        return false;
    }

}