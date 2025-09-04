package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Faculty;
import com.college.attendance.attendance.system.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    // Get all faculty
    @GetMapping
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    // Add new faculty
    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Get single faculty by id
    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }
}
