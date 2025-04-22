package com.helpu.classclue.models;

import com.google.firebase.database.PropertyName;
import java.util.HashMap;
import java.util.Map;

public class Subject {
    private String id;
    private String code;
    private String name;
    private String description;
    private String semester;
    private int credit;

    public Subject() {}  // Needed for Firebase

    public Subject(String code, String name, String description, String semester, int credit) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.semester = semester;
        this.credit = credit;
    }

    // Getters and Setters
    @PropertyName("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @PropertyName("code")
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @PropertyName("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @PropertyName("description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @PropertyName("semester")
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    @PropertyName("credit")
    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", name);
        map.put("description", description);
        map.put("semester", semester);
        map.put("credit",credit);
        return map;
    }
}