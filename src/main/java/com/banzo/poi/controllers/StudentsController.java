package com.banzo.poi.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banzo.poi.services.DataToExcel;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private DataToExcel dataToExcel;

    @RequestMapping("/excel")
    public ResponseEntity<?> getStudents() {
        String fileName = "students.xlsx";
        ByteArrayInputStream excelData = dataToExcel.studentsToExcel();
        if (excelData != null) {
            try {
                byte[] bytes = IOUtils.toByteArray(excelData);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                        .body(bytes);
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error reading the Excel file.");
            }
        }
        return ResponseEntity.badRequest().body("Error creating the excel file");
    }
}
