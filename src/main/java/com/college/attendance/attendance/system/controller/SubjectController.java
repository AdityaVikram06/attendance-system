package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Subject;
import com.college.attendance.attendance.system.model.Faculty;
import com.college.attendance.attendance.system.repository.SubjectRepository;
import com.college.attendance.attendance.system.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    // Get all subjects
    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Add new subject (with facultyId)
    @PostMapping
    public Subject addSubject(@RequestParam String name, @RequestParam Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Subject subject = new Subject(name, faculty);
        return subjectRepository.save(subject);
    }

    // Get subject by ID
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    // Update subject
    @PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam Long facultyId) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        subject.setName(name);
        subject.setFaculty(faculty);

        return subjectRepository.save(subject);
    }

    // Delete subject
    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectRepository.deleteById(id);
        return "Subject with ID " + id + " deleted successfully!";
    }
}
