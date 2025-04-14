package com.example.modulus.FragmentCalendar;


import android.graphics.Bitmap;
import com.framgia.library.calendardayview.data.IPopup;
import java.util.Calendar;

public class Popup implements IPopup{

    private Calendar startTime;
    private Calendar endTime;

    private String imageStart;
    private String imageEnd;

    private String description;

    private String quote;

    private String title;

    private int Id;
    private Calendar eventDate;
    private String name;

    private int color;


    public boolean isOnDate(Calendar date) {
        return this.eventDate.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                this.eventDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                this.eventDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH);
    }

    //Constructor
    public Popup(int Id, Calendar eventDate, Calendar startTime, Calendar endTime, String title, String description, int color){
        this.Id = Id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.color = color;
        this.eventDate = eventDate;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public String getDescription() {
        return description;
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



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }
}