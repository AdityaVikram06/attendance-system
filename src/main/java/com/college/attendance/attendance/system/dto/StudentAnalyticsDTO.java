package com.college.attendance.attendance.system.dto;

public class StudentAnalyticsDTO {
    private Long studentId;
    private Long subjectId; // null if overall
    private long totalClasses;
    private long present;
    private long absent;
    private double percentage;

    // Getters & Setters
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public Long getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
    public long getTotalClasses() {
        return totalClasses;
    }
    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
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
