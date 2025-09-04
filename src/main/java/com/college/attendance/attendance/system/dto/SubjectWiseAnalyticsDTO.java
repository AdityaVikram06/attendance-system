package com.college.attendance.attendance.system.dto;

public class SubjectWiseAnalyticsDTO {
    private Long subjectId;
    private String subjectName;
    private long totalClasses;
    private long totalStudentsPresent;
    private long totalStudentsAbsent;
    private double averagePercentage;

    public SubjectWiseAnalyticsDTO(Long subjectId, String subjectName, long totalClasses,
                                   long totalStudentsPresent, long totalStudentsAbsent, double averagePercentage) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.totalClasses = totalClasses;
        this.totalStudentsPresent = totalStudentsPresent;
        this.totalStudentsAbsent = totalStudentsAbsent;
        this.averagePercentage = averagePercentage;
    }

    // getters and setters
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public long getTotalClasses() { return totalClasses; }
    public void setTotalClasses(long totalClasses) { this.totalClasses = totalClasses; }

    public long getTotalStudentsPresent() { return totalStudentsPresent; }
    public void setTotalStudentsPresent(long totalStudentsPresent) { this.totalStudentsPresent = totalStudentsPresent; }

    public long getTotalStudentsAbsent() { return totalStudentsAbsent; }
    public void setTotalStudentsAbsent(long totalStudentsAbsent) { this.totalStudentsAbsent = totalStudentsAbsent; }

    public double getAveragePercentage() { return averagePercentage; }
    public void setAveragePercentage(double averagePercentage) { this.averagePercentage = averagePercentage; }
}
