package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
