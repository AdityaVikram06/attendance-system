package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.model.Subject;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import com.college.attendance.attendance.system.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:3000")
public class FacultyController {

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // ✅ Get all classes for a faculty
    @GetMapping("/{facultyId}/classes")
    public List<ClassSession> getFacultyClasses(@PathVariable Long facultyId) {
        return classSessionRepository.findBySubject_Faculty_Id(facultyId);
    }

    // ✅ Generate manual code
    private String generateManualCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // ✅ Lookup class by manual code
    @GetMapping("/session/{manualCode}")
    public ClassSession getSessionByManualCode(@PathVariable String manualCode) {
        return classSessionRepository.findByManualCode(manualCode)
                .orElseThrow(() -> new RuntimeException("Invalid manual code"));
    }

    // ✅ Get currently active classes
    @GetMapping("/{facultyId}/active-classes")
    public List<ClassSession> getActiveClasses(@PathVariable Long facultyId) {
        LocalDateTime now = LocalDateTime.now();
        return classSessionRepository.findBySubject_Faculty_Id(facultyId).stream()
                .filter(session -> !session.getStartTime().isAfter(now) && !session.getEndTime().isBefore(now))
                .toList();
    }

    // ✅ Add a new class session
    @PostMapping("/addClass")
    public ClassSession addClass(@RequestParam Long subjectId,
                                 @RequestParam String topic,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        ClassSession session = new ClassSession();
        session.setTopic(topic);
        session.setSubject(subject);
        session.setStartTime(LocalDateTime.parse(startTime));
        session.setEndTime(LocalDateTime.parse(endTime));

        session.setQrExpiryTime(session.getStartTime().plusMinutes(10));
        session.setManualCode(generateManualCode());

        return classSessionRepository.save(session);
    }
}
