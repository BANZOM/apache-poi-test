package com.banzo.poi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banzo.poi.entities.ExcelUploads;
import com.banzo.poi.repository.ExcelUploadsRepository;

@Service
public class ExcelUploadService {

    @Autowired
    private ExcelUploadsRepository excelUploadsRepository;

    public ExcelUploads upload(byte[] file) {
        return excelUploadsRepository.save(new ExcelUploads(file));
    }

}