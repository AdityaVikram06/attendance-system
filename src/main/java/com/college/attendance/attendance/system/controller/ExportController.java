package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.model.Attendance;
import com.college.attendance.attendance.system.repository.AttendanceRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
public class ExportController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportAttendancePdf() {
        try {
            List<Attendance> attendances = attendanceRepository.findAll();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph("Attendance Report"));
            document.add(new Paragraph(" ")); // blank line

            PdfPTable table = new PdfPTable(5); // 5 columns
            table.addCell("ID");
            table.addCell("Student Name");
            table.addCell("Roll Number");
            table.addCell("Subject");
            table.addCell("Present");

            for (Attendance att : attendances) {
                table.addCell(String.valueOf(att.getId()));
                table.addCell(att.getStudent().getName());
                table.addCell(att.getStudent().getRollNumber());
                table.addCell(att.getClassSession().getSubject().getName());
                table.addCell(att.isPresent() ? "Yes" : "No");
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
