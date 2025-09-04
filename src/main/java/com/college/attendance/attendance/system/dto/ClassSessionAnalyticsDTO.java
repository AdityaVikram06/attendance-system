package com.college.attendance.attendance.system.dto;

public class ClassSessionAnalyticsDTO {
    private Long classId;
    private Long subjectId;
    private String subjectName;
    private long totalStudents;
    private long presentStudents;
    private long absentStudents;

    public ClassSessionAnalyticsDTO(Long classId, Long subjectId, String subjectName,
                                    long totalStudents, long presentStudents, long absentStudents) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.totalStudents = totalStudents;
        this.presentStudents = presentStudents;
        this.absentStudents = absentStudents;
    }

    // getters and setters
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getPresentStudents() { return presentStudents; }
    public void setPresentStudents(long presentStudents) { this.presentStudents = presentStudents; }

    public long getAbsentStudents() { return absentStudents; }
    public void setAbsentStudents(long absentStudents) { this.absentStudents = absentStudents; }
}
