package com.helpu.classclue.models;

public class Subject {
    private String code;
    private String name;
    private String description;
    private String semester;
    private int credits;

    // Constructor
    public Subject(String code, String name, String description, String semester, int credits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.semester = semester;
        this.credits = credits;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSemester() {
        return semester;
    }

    public int getCredits() {
        return credits;
    }

    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    // Overriding toString() for easy representation
    @Override
    public String toString() {
        return "Subject{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                '}';
    }
}


