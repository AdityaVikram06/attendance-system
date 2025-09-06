package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Attendance;
import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.model.Student;
import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import com.college.attendance.attendance.system.repository.StudentRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // ✅ Generate QR code for a class session (valid for 10 minutes)
    @GetMapping("/class/{classId}")
    public ResponseEntity<byte[]> generateQr(@PathVariable Long classId) throws Exception {
        ClassSession classSession = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Set expiry = now + 10 minutes
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        classSession.setQrExpiryTime(expiryTime);
        classSessionRepository.save(classSession);

        String qrContent = "classId=" + classId;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitMatrix matrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=class-" + classId + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(baos.toByteArray());
    }

    // ✅ Mark attendance with expiry validation
    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendanceFromQr(
            @RequestParam Long studentId,
            @RequestParam Long classId
    ) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ClassSession classSession = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class session not found"));

        // ❌ Reject if QR expired
        if (classSession.getQrExpiryTime() == null || LocalDateTime.now().isAfter(classSession.getQrExpiryTime())) {
            throw new RuntimeException("QR Code expired! Please request a new one.");
        }

        // Check if already marked
        boolean alreadyMarked = attendanceRepository.findByStudent_Id(studentId).stream()
                .anyMatch(a -> a.getClassSession().getId().equals(classId));

        if (alreadyMarked) {
            throw new RuntimeException("Attendance already marked for this session!");
        }

        Attendance attendance = new Attendance(student, classSession, "PRESENT", LocalDateTime.now());
        Attendance saved = attendanceRepository.save(attendance);

        return ResponseEntity.ok(saved);
    }

}
