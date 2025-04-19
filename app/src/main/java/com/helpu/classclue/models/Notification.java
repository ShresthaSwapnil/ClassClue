package com.helpu.classclue.models;

import java.util.Date;

public class Notification {
    private String title;
    private String message;
    private Date timestamp;
    private boolean isRead;

    // Constructor
    public Notification(String title, String message, Date timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public void markAsRead() { isRead = true; }
}