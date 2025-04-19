package com.helpu.classclue.models;

public class EventItem {
    private String dueDate;
    private String timeRange;
    private String subjectName;
    private String taskCount;

    public EventItem(String dueDate, String timeRange, String subjectName, String taskCount) {
        this.dueDate = dueDate;
        this.timeRange = timeRange;
        this.subjectName = subjectName;
        this.taskCount = taskCount;
    }

    // Getters
    public String getDueDate() { return dueDate; }
    public String getTimeRange() { return timeRange; }
    public String getSubjectName() { return subjectName; }
    public String getTaskCount() { return taskCount; }
}