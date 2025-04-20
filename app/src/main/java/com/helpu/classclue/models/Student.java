package com.helpu.classclue.models;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String semester;

    // Constructor
    public Student(String studentId, String name, String email, String semester) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.semester = semester;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSemester() {
        return semester;
    }

    // Setters
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    // Overriding toString() for easy representation
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }
}

