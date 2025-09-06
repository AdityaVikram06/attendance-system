package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    List<Subject> findByStudents_Id(Long studentId);


    List<Subject> findByFaculty_Id(Long facultyId);
}
