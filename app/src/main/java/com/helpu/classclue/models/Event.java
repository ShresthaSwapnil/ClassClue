package com.helpu.classclue.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.PropertyName;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "location")
    private String location;
    @Ignore
    private boolean reminder_24h;
    @Ignore
    private boolean reminder_2h;
    @ColumnInfo(name = "subjectId")
    private String subjectId;

    public Event() {}  // Needed for Firebase

    public Event(String title, String subjectId, String date, String time, String location) {
        this.title = title;
        this.subjectId = subjectId;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subject_id) { this.subjectId = subject_id; }
}