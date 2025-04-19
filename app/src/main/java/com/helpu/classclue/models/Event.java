package com.helpu.classclue.models;

public class Event {
    private String subjectName;
    private String dueDate;
    private String timeRange;
    private int taskCount;

    // Constructor
    public Event(String subjectName, String dueDate, String timeRange, int taskCount) {
        this.subjectName = subjectName;
        this.dueDate = dueDate;
        this.timeRange = timeRange;
        this.taskCount = taskCount;
    }

    // Getters
    public String getSubjectName() {
        return subjectName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public int getTaskCount() {
        return taskCount;
    }

    // Setters
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    // Overriding toString() for easy representation
    @Override
    public String toString() {
        return "Event{" +
                "subjectName='" + subjectName + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", timeRange='" + timeRange + '\'' +
                ", taskCount=" + taskCount +
                '}';
    }
}

