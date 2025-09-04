package com.college.attendance.attendance.system.dto;

import java.util.Map;

public class LowAttendanceDTO {
    private Long subjectId;
    private double threshold;
    private Map<Long, Double> lowAttendanceStudents; // studentId -> percentage

    // Getters & Setters
    public Long getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public Map<Long, Double> getLowAttendanceStudents() {
        return lowAttendanceStudents;
    }
    public void setLowAttendanceStudents(Map<Long, Double> lowAttendanceStudents) {
        this.lowAttendanceStudents = lowAttendanceStudents;
    }
}
