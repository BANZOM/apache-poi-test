package com.banzo.poi.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banzo.poi.entities.Student;
import com.banzo.poi.repository.StudentRepository;

@Service
public class DataToExcel {

    @Autowired
    private StudentRepository studentRepository;

    private static final String[] HEADERS = {
        "ID",
        "Name",
        "Email",
        "Phone",
        "University Name",
        "University Address",
        "University Phone"
    };

    private static final String SHEET_NAME = "Student_Data";

    public ByteArrayInputStream studentsToExcel() {
        List<Student> students = studentRepository.findAll();
        try(Workbook wb = new XSSFWorkbook()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = wb.createSheet(SHEET_NAME);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            int rowNum = 1;

            for (Student student : students) {
                Row row = sheet.createRow(rowNum);

                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getEmail());
                row.createCell(3).setCellValue(student.getPhone());
                row.createCell(4).setCellValue(student.getUniversity().getName());
                row.createCell(5).setCellValue(student.getUniversity().getAddress());
                row.createCell(6).setCellValue(student.getUniversity().getPhone());

                rowNum++;
            }

            // auto size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }


            wb.write(out);

            return new ByteArrayInputStream(out.toByteArray());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage());
        } 
    }
}
