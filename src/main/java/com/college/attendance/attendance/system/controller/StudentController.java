package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Student;
import com.college.attendance.attendance.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add new student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Update attendance
    @PutMapping("/{id}/attendance")
    public Student updateAttendance(@PathVariable Long id, @RequestParam boolean present) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setPresent(present);
        return studentRepository.save(student);
    }
}