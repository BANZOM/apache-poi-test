package com.banzo.poi.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banzo.poi.entities.ExcelUploads;
import com.banzo.poi.services.DataToExcel;
import com.banzo.poi.services.ExcelUploadService;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private DataToExcel dataToExcel;

    @Autowired
    private ExcelUploadService excelUploadService;

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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStudents(RequestEntity<byte[]> requestEntity) {

        HttpHeaders headers = requestEntity.getHeaders();
        List<String> contentType = headers.get("Content-Type");

        if (contentType == null || contentType.isEmpty()
                || !contentType.get(0).trim().equals("application/vnd.ms-excel;charset=UTF-8")
                        && !contentType.get(0).trim().equals(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8")) {
            return ResponseEntity.badRequest().body("Invalid content type. Only Excel files are allowed.");
        }

        byte[] file = requestEntity.getBody();

        if (file == null || file.length == 0) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(file)) {
            // it'll throw an exception if the file is not a valid Excel file
            WorkbookFactory.create(bis);

            ExcelUploads excel = excelUploadService.upload(file);

            return ResponseEntity.ok("File uploaded successfully with id: " + excel.getId() + "; Upload timestamp: "
                    + excel.getUploadtTime());
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("Error reading the Excel file or the file is not a valid Excel file.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong");
        } 
    }
}
