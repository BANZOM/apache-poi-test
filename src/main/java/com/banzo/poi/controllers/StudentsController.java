package com.banzo.poi.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.banzo.poi.entities.ExcelUploads;
import com.banzo.poi.services.DataToExcel;
import com.banzo.poi.services.ExcelUploadService;

@RestController
@RequestMapping("/api/students")
public class StudentsController {

    private static final Logger logger = LoggerFactory.getLogger(StudentsController.class);

    @Autowired
    private DataToExcel dataToExcel;

    @Autowired
    private ExcelUploadService excelUploadService;

    @GetMapping("/excel")
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
    public ResponseEntity<String> uploadStudents(@RequestParam(value = "file", required = false) MultipartFile file) {

        logger.info("Received file: " + file.getContentType());

        if (null == file || file.isEmpty()) {
            logger.warn("File is empty or null.");
            return ResponseEntity.badRequest().body("No file uploaded. File is empty or null.");
        }

        String fileName = file.getOriginalFilename();
        logger.info("File name: '" + fileName + "'");

        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            logger.info("Provided file is not an Excel file.");
            return ResponseEntity.badRequest().body("Invalid file. Only Excel files are allowed.");
        }

        try {
            byte[] excel = file.getBytes();
            try (ByteArrayInputStream bis = new ByteArrayInputStream(excel)) {
                // it'll throw an exception if the file is not a valid Excel file
                WorkbookFactory.create(bis);
                boolean isUploaded = excelUploadService.upload(excel);
                if (isUploaded) {
                    logger.info(fileName + " uploaded successfully.");
                    return ResponseEntity.ok("File uploaded successfully");
                } else {
                    logger.error("Error uploading the file.");
                    return ResponseEntity.status(500).body("Error uploading the file");
                }
            }
        } catch (IOException e) {
            logger.error("Error reading the Excel file or the file is not a valid Excel file.");
            return ResponseEntity.status(500)
                    .body("Error reading the Excel file or the file is not a valid Excel file.");

        } catch (Exception e) {
            logger.error("Something went wrong.");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncStudents() {
        return ResponseEntity.ok("Students synchronized successfully");
    }
}
