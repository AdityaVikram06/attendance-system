package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Subject;
import com.college.attendance.attendance.system.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    // Get all subjects
    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Add new subject
    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }

    // Get subject by id
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    // Update subject
    @PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id, @RequestBody Subject updatedSubject) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.setName(updatedSubject.getName());
        subject.setFacultyName(updatedSubject.getFacultyName());

        return subjectRepository.save(subject);
    }

    // Delete subject
    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectRepository.deleteById(id);
        return "Subject with ID " + id + " deleted successfully!";
    }
}
