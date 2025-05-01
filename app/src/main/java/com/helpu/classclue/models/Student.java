package com.helpu.classclue.models;

import com.google.firebase.database.PropertyName;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String semester;

    // Constructor
    public Student() {}
    public Student(String studentId, String name, String email, String semester) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.semester = semester;
    }

    // Getters
    @PropertyName("studentId")
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName("semester")
    public String getSemester() {
        return semester;
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
                ", intake='" + semester + '\'' +
                '}';
    }
}

