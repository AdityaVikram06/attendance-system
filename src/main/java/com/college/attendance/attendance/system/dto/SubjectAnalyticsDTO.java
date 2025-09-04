package com.college.attendance.attendance.system.dto;

public class SubjectAnalyticsDTO {
    private Long subjectId;
    private String subjectName;
    private long totalRecords;
    private long present;
    private long absent;
    private double percentage;

    // Getters & Setters
    public Long getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public long getTotalRecords() {
        return totalRecords;
    }
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
    public long getPresent() {
        return present;
    }
    public void setPresent(long present) {
        this.present = present;
    }
    public long getAbsent() {
        return absent;
    }
    public void setAbsent(long absent) {
        this.absent = absent;
    }
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
