package com.helpu.classclue.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String name;
    private String email;
    private String role;
    private String notification_sound;
    private Map<String, Boolean> subjects = new HashMap<>();

    public User() {}  // Needed for Firebase

    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    @Exclude
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    @PropertyName("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @PropertyName("email")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @PropertyName("role")
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @PropertyName("notification_sound")
    public String getNotificationSound() { return notification_sound; }
    public void setNotificationSound(String notification_sound) {
        this.notification_sound = notification_sound;
    }

    @PropertyName("subjects")
    public Map<String, Boolean> getSubjects() { return subjects; }
    public void setSubjects(Map<String, Boolean> subjects) {
        this.subjects = subjects;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("role", role);
        map.put("notification_sound", notification_sound);
        map.put("subjects", subjects);
        return map;
    }
}