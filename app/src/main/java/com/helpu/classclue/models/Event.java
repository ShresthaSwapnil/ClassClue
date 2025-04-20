package com.helpu.classclue.models;

public class Event {
    private String title;
    private String subject;
    private String date;
    private String time;
    private String location;

    // Constructor
    public Event(String title, String subject, String date, String time, String location) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Overriding toString() for easy representation
    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}


