package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // find by student (nested property)
    List<Attendance> findByStudent_Id(Long studentId);

    // find by class session (nested property)
    List<Attendance> findByClassSession_Id(Long classId);

    // count present in a class
    long countByClassSession_IdAndStatus(Long classId, String status);

    // count all by class
    long countByClassSession_Id(Long classId);
}
