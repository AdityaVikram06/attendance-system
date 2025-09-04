package com.college.attendance.attendance.system.repository;

import com.college.attendance.attendance.system.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudent_Id(Long studentId);

    List<Attendance> findByClassSession_Id(Long classId);

    long countByClassSession_IdAndStatus(Long classId, String status);

    long countByClassSession_Id(Long classId);

    // --- subject-level queries ---
    List<Attendance> findByClassSession_Subject_Id(Long subjectId);

    List<Attendance> findByClassSession_Subject_IdAndStudent_Id(Long subjectId, Long studentId);

    long countByClassSession_Subject_Id(Long subjectId);

    long countByClassSession_Subject_IdAndStatus(Long subjectId, String status);
}
