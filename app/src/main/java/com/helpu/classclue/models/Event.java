package com.helpu.classclue.models;

import com.google.firebase.database.PropertyName;

public class Event {
    private String title;
    private String date;
    private String time;
    private String location;
    private boolean reminder_24h;
    private boolean reminder_2h;
    private String subject_id;

    public Event() {}  // Needed for Firebase

    public Event(String title, String subject_id, String date, String time, String location) {
        this.title = title;
        this.subject_id = subject_id;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters and Setters

    @PropertyName("title")
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @PropertyName("date")
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @PropertyName("time")
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @PropertyName("location")
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @PropertyName("reminder24h")
    public boolean isReminder24h() { return reminder_24h; }
    public void setReminder24h(boolean reminder_24h) { this.reminder_24h = reminder_24h; }

    @PropertyName("reminder2h")
    public boolean isReminder2h() { return reminder_2h; }
    public void setReminder2h(boolean reminder_2h) { this.reminder_2h = reminder_2h; }

    @PropertyName("subjectId")
    public String getSubjectId() { return subject_id; }
    public void setSubjectId(String subject_id) { this.subject_id = subject_id; }
}