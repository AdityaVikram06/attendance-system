package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.college.attendance.attendance.system.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // --- Student Analytics ---
    @GetMapping("/student/{studentId}")
    public Map<String, Object> getStudentAnalytics(@PathVariable Long studentId) {
        long total = attendanceRepository.findByStudent_Id(studentId).size();
        long present = attendanceRepository.findByStudent_Id(studentId).stream()
                .filter(att -> "PRESENT".equalsIgnoreCase(att.getStatus()))
                .count();
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("totalClasses", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- Class Analytics ---
    @GetMapping("/class/{classId}")
    public Map<String, Object> getClassAnalytics(@PathVariable Long classId) {
        long total = attendanceRepository.findByClassSession_Id(classId).size();
        long present = attendanceRepository.findByClassSession_Id(classId).stream()
                .filter(att -> "PRESENT".equalsIgnoreCase(att.getStatus()))
                .count();
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("classId", classId);
        result.put("totalAttendanceRecords", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- Low Attendance Students ---
    @GetMapping("/low-attendance/{classId}")
    public Map<String, Object> getLowAttendanceStudents(
            @PathVariable Long classId,
            @RequestParam(defaultValue = "75") double threshold) {

        Map<Long, Double> studentPercentages = new HashMap<>();

        for (Attendance att : attendanceRepository.findByClassSession_Id(classId)) {
            Long studentId = att.getStudent().getId();
            long totalClasses = attendanceRepository.findByStudent_Id(studentId).size();
            long present = attendanceRepository.findByStudent_Id(studentId).stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                    .count();

            double percentage = totalClasses > 0 ? (present * 100.0 / totalClasses) : 0.0;
            studentPercentages.put(studentId, percentage);
        }

        Map<Long, Double> lowStudents = new HashMap<>();
        for (Map.Entry<Long, Double> entry : studentPercentages.entrySet()) {
            if (entry.getValue() < threshold) {
                lowStudents.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("classId", classId);
        result.put("threshold", threshold);
        result.put("lowAttendanceStudents", lowStudents);

        return result;
    }
}
