package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
}
