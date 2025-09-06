package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    List<ClassSession> findBySubject_Faculty_Id(Long facultyId);
    Optional<ClassSession> findByManualCode(String manualCode);
    boolean existsByManualCode(String manualCode);
    List<ClassSession> findBySubject_Id(Long subjectId);

}
