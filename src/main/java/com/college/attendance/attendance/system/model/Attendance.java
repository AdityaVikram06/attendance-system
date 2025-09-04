package com.college.attendance.attendance.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to Student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Link to ClassSession
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassSession classSession;

    @Column(nullable = false)
    private String status;  // PRESENT / ABSENT

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Attendance() {
    }

    public Attendance(Student student, ClassSession classSession, String status, LocalDateTime timestamp) {
        this.student = student;
        this.classSession = classSession;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassSession getClassSession() {
        return classSession;
    }

    public void setClassSession(ClassSession classSession) {
        this.classSession = classSession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
