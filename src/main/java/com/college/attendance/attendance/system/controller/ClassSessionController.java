package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class-sessions")   // <---- use this in Postman, not /classes
public class ClassSessionController {

    @Autowired
    private ClassSessionRepository classSessionRepository;

    // Get all sessions
    @GetMapping
    public List<ClassSession> getAllSessions() {
        return classSessionRepository.findAll();
    }

    // Create new class session
    @PostMapping
    public ClassSession createSession(@RequestBody ClassSession session) {
        return classSessionRepository.save(session);
    }

    // Get one session by id
    @GetMapping("/{id}")
    public ClassSession getSession(@PathVariable Long id) {
        return classSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClassSession not found"));
    }

    // Delete session
    @DeleteMapping("/{id}")
    public String deleteSession(@PathVariable Long id) {
        classSessionRepository.deleteById(id);
        return "Deleted session with id: " + id;
    }
}
