package com.college.attendance.attendance.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_sessions")
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column
    private LocalDateTime qrExpiryTime;

    @Column(unique = true)
    private String manualCode; // <-- add this

    // 🔗 Many sessions ↔ One subject
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public ClassSession() {}

    public ClassSession(String topic, LocalDateTime startTime, LocalDateTime endTime, Subject subject) {
        this.topic = topic;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public LocalDateTime getQrExpiryTime() { return qrExpiryTime; }
    public void setQrExpiryTime(LocalDateTime qrExpiryTime) { this.qrExpiryTime = qrExpiryTime; }

    public String getManualCode() { return manualCode; }   // <-- getter
    public void setManualCode(String manualCode) { this.manualCode = manualCode; } // <-- setter

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
}
