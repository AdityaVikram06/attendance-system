package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Attendance;
import com.college.attendance.attendance.system.model.Subject;
import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.college.attendance.attendance.system.repository.SubjectRepository;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    // --- 1. Student Overall Analytics ---
    @GetMapping("/student/{studentId}")
    public Map<String, Object> getStudentAnalytics(@PathVariable Long studentId) {
        List<Attendance> records = attendanceRepository.findByStudent_Id(studentId);
        long total = records.size();
        long present = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("totalClasses", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- 2. Student Analytics Per Subject ---
    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public Map<String, Object> getStudentSubjectAnalytics(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {

        List<Attendance> records =
                attendanceRepository.findByClassSession_Subject_IdAndStudent_Id(subjectId, studentId);
        long total = records.size();
        long present = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("subjectId", subjectId);
        result.put("totalClasses", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- 3. Subject Analytics (all students) ---
    @GetMapping("/subject/{subjectId}")
    public Map<String, Object> getSubjectAnalytics(@PathVariable Long subjectId) {
        long total = attendanceRepository.countByClassSession_Subject_Id(subjectId);
        long present = attendanceRepository.countByClassSession_Subject_IdAndStatus(subjectId, "PRESENT");
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("subjectId", subjectId);
        result.put("totalRecords", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- 4. Low Attendance Students (per subject) ---
    @GetMapping("/low-attendance/subject/{subjectId}")
    public Map<String, Object> getLowAttendanceStudentsBySubject(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "75") double threshold) {

        List<Attendance> subjectRecords = attendanceRepository.findByClassSession_Subject_Id(subjectId);

        // Group by studentId
        Map<Long, List<Attendance>> studentRecords = new HashMap<>();
        for (Attendance att : subjectRecords) {
            Long studentId = att.getStudent().getId();
            studentRecords.computeIfAbsent(studentId, k -> new ArrayList<>()).add(att);
        }

        // Calculate percentages
        Map<Long, Double> lowStudents = new HashMap<>();
        for (Map.Entry<Long, List<Attendance>> entry : studentRecords.entrySet()) {
            long total = entry.getValue().size();
            long present = entry.getValue().stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                    .count();
            double percentage = total > 0 ? (present * 100.0 / total) : 0.0;

            if (percentage < threshold) {
                lowStudents.put(entry.getKey(), percentage);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("subjectId", subjectId);
        result.put("threshold", threshold);
        result.put("lowAttendanceStudents", lowStudents);

        return result;
    }

    // --- 5. Subject Summary (with subject + faculty name) ---
    @GetMapping("/subject/{subjectId}/summary")
    public Map<String, Object> getSubjectSummary(@PathVariable Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        long total = attendanceRepository.countByClassSession_Subject_Id(subjectId);
        long present = attendanceRepository.countByClassSession_Subject_IdAndStatus(subjectId, "PRESENT");
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("subjectId", subject.getId());
        result.put("subjectName", subject.getName());
        result.put("faculty", subject.getFaculty().getName()); // ✅ fixed
        result.put("totalRecords", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

        return result;
    }

    // --- 6. Class Session Analytics (students present/absent in one class) ---
    @GetMapping("/class/{classId}")
    public Map<String, Object> getClassAnalytics(@PathVariable Long classId) {
        ClassSession session = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        long total = attendanceRepository.countByClassSession_Id(classId);
        long present = attendanceRepository.countByClassSession_IdAndStatus(classId, "PRESENT");
        long absent = total - present;

        Map<String, Object> result = new HashMap<>();
        result.put("classId", session.getId());
        result.put("subject", session.getSubject().getName());
        result.put("faculty", session.getSubject().getFaculty().getName()); // ✅ added
        result.put("startTime", session.getStartTime());
        result.put("endTime", session.getEndTime());
        result.put("totalStudents", total);
        result.put("present", present);
        result.put("absent", absent);

        return result;
    }
    // --- 7. Admin: All Subjects Overview ---
    @GetMapping("/subjects/overview")
    public List<Map<String, Object>> getAllSubjectsOverview() {
        List<Subject> subjects = subjectRepository.findAll();
        List<Map<String, Object>> overviewList = new ArrayList<>();

        for (Subject subject : subjects) {
            long total = attendanceRepository.countByClassSession_Subject_Id(subject.getId());
            long present = attendanceRepository.countByClassSession_Subject_IdAndStatus(subject.getId(), "PRESENT");
            long absent = total - present;

            Map<String, Object> data = new HashMap<>();
            data.put("subjectId", subject.getId());
            data.put("subjectName", subject.getName());
            data.put("faculty", subject.getFaculty().getName());
            data.put("totalRecords", total);
            data.put("present", present);
            data.put("absent", absent);
            data.put("percentage", total > 0 ? (present * 100.0 / total) : 0.0);

            overviewList.add(data);
        }

        return overviewList;
    }
    // --- 8. Admin: All Students Overview ---
    @GetMapping("/students/overview")
    public List<Map<String, Object>> getAllStudentsOverview() {
        // Fetch distinct student IDs from attendance records
        List<Attendance> allRecords = attendanceRepository.findAll();
        Map<Long, List<Attendance>> studentMap = new HashMap<>();

        for (Attendance att : allRecords) {
            Long studentId = att.getStudent().getId();
            studentMap.computeIfAbsent(studentId, k -> new ArrayList<>()).add(att);
        }

        List<Map<String, Object>> overviewList = new ArrayList<>();

        for (Map.Entry<Long, List<Attendance>> entry : studentMap.entrySet()) {
            Long studentId = entry.getKey();
            List<Attendance> records = entry.getValue();

            long total = records.size();
            long present = records.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                    .count();
            long absent = total - present;
            double percentage = total > 0 ? (present * 100.0 / total) : 0.0;

            Map<String, Object> data = new HashMap<>();
            data.put("studentId", studentId);
            data.put("studentName", records.get(0).getStudent().getName()); // from Student entity
            data.put("rollNumber", records.get(0).getStudent().getRollNumber());
            data.put("totalClasses", total);
            data.put("present", present);
            data.put("absent", absent);
            data.put("percentage", percentage);

            overviewList.add(data);
        }

        return overviewList;
    }
    // --- 9. Student: Classes Attended Count ---
    @GetMapping("/student/{studentId}/classes-attended")
    public Map<String, Object> getClassesAttended(@PathVariable Long studentId) {
        List<Attendance> records = attendanceRepository.findByStudent_Id(studentId);
        long attended = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("classesAttended", attended);
        result.put("totalClasses", records.size());
        return result;
    }

    // --- 10. Student: Classes Today ---
    @GetMapping("/student/{studentId}/classes-today")
    public List<ClassSession> getClassesToday(@PathVariable Long studentId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Fetch student’s subjects
        List<Subject> subjects = subjectRepository.findByStudents_Id(studentId);

        // Find all classes for those subjects today
        List<ClassSession> sessions = new ArrayList<>();
        for (Subject sub : subjects) {
            List<ClassSession> classList = classSessionRepository.findBySubject_Id(sub.getId());
            for (ClassSession cs : classList) {
                if (cs.getStartTime().isAfter(startOfDay) && cs.getStartTime().isBefore(endOfDay)) {
                    sessions.add(cs);
                }
            }
        }
        return sessions;
    }

    // --- 11. Student: Active Classes Right Now ---
    @GetMapping("/student/{studentId}/active-classes")
    public List<ClassSession> getActiveClasses(@PathVariable Long studentId) {
        LocalDateTime now = LocalDateTime.now();

        List<Subject> subjects = subjectRepository.findByStudents_Id(studentId);

        List<ClassSession> sessions = new ArrayList<>();
        for (Subject sub : subjects) {
            List<ClassSession> classList = classSessionRepository.findBySubject_Id(sub.getId());
            for (ClassSession cs : classList) {
                if (!cs.getStartTime().isAfter(now) && !cs.getEndTime().isBefore(now)) {
                    sessions.add(cs);
                }
            }
        }
        return sessions;
    }


    // --- 12. Student: Recent Attendance (last N records) ---
    @GetMapping("/student/{studentId}/recent-attendance")
    public List<Attendance> getRecentAttendance(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "5") int limit) {
        List<Attendance> records = attendanceRepository.findByStudent_Id(studentId);
                records.sort(Comparator.comparing(a -> a.getClassSession().getStartTime(), Comparator.reverseOrder()));

                return records.stream().limit(limit).toList();
    }



}
