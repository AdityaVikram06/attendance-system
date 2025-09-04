package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Attendance;
import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.model.Student;
import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import com.college.attendance.attendance.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }



    @PostMapping
    public Attendance markAttendance(@RequestParam Long studentId,
                                     @RequestParam Long classId,
                                     @RequestParam(defaultValue = "PRESENT") String status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ClassSession classSession = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Attendance attendance = new Attendance(
                student,
                classSession,
                status.toUpperCase(),
                LocalDateTime.now()
        );

        return attendanceRepository.save(attendance);
    }
}
