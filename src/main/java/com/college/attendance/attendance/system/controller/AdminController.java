package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // ✅ Dashboard system statistics
    @GetMapping("/system-stats")
    public Map<String, Long> getSystemStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("students", studentRepository.count());
        stats.put("faculties", facultyRepository.count());
        stats.put("subjects", subjectRepository.count());
        stats.put("classes", classSessionRepository.count());
        stats.put("attendanceRecords", attendanceRepository.count());
        return stats;
    }
}
