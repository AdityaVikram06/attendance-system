package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Faculty;
import com.college.attendance.attendance.system.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    // Get all faculties
    @GetMapping
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    // Add new faculty
    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Get faculty by id
    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    // Update faculty
    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty updatedFaculty) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        faculty.setName(updatedFaculty.getName());
        faculty.setEmail(updatedFaculty.getEmail());

        return facultyRepository.save(faculty);
    }

    // Delete faculty
    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyRepository.deleteById(id);
        return "Faculty with ID " + id + " deleted successfully!";
    }
}
