package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Attendance;
import com.college.attendance.attendance.system.model.ClassSession;
import com.college.attendance.attendance.system.model.Student;
import com.college.attendance.attendance.system.model.Subject;
import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.college.attendance.attendance.system.repository.ClassSessionRepository;
import com.college.attendance.attendance.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/student") // ⚠ changed to singular to match frontend
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;
    private ClassSessionRepository classSessionRepository;

    // ✅ Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ✅ Add new student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // ✅ Update attendance manually
    @PutMapping("/{id}/attendance")
    public Student updateAttendance(@PathVariable Long id, @RequestParam boolean present) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setPresent(present);
        return studentRepository.save(student);
    }

    // ✅ Get subjects for a student
    @GetMapping("/{id}/subjects")
    public Set<Subject> getStudentSubjects(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getSubjects();
    }
    

    // ✅ Get attendance history for a student
    @GetMapping("/{id}/attendance-history")
    public List<Attendance> getStudentAttendanceHistory(@PathVariable Long id) {
        return attendanceRepository.findByStudent_Id(id);
    }
    @PostMapping("/{studentId}/join-session/{manualCode}")
    public String joinSessionWithCode(@PathVariable Long studentId, @PathVariable String manualCode) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ClassSession session = classSessionRepository.findByManualCode(manualCode)
                .orElseThrow(() -> new RuntimeException("Invalid code"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setClassSession(session);
        attendance.setPresent(true);

        attendanceRepository.save(attendance);

        return "Attendance marked successfully via manual code!";
    }


}
