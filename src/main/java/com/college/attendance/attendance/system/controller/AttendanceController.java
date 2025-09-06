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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    // ✅ Get all attendance records
    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // ✅ Mark attendance (base method)
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

    // ✅ Alias (frontend calls /attendance/mark)
    @PostMapping("/mark")
    public Attendance markAttendanceAlias(@RequestParam Long studentId,
                                          @RequestParam Long classId,
                                          @RequestParam(defaultValue = "PRESENT") String status) {
        return markAttendance(studentId, classId, status);
    }

    // ✅ Generate QR (faculty side)
    @PostMapping("/generate-qr")
    public Map<String, Object> generateQr(@RequestParam Long classId) {
        ClassSession classSession = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        String token = "QR-" + classId + "-" + System.currentTimeMillis();
        classSession.setQrExpiryTime(LocalDateTime.now().plusMinutes(5));
        classSessionRepository.save(classSession);

        Map<String, Object> response = new HashMap<>();
        response.put("qrToken", token);
        response.put("expiry", classSession.getQrExpiryTime());
        return response;
    }

    // ✅ Validate QR
    @PostMapping("/validate-qr")
    public Map<String, Object> validateQr(@RequestParam String qrToken) {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", qrToken.startsWith("QR-"));
        return response;
    }
}
