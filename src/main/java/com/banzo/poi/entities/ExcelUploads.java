package com.banzo.poi.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "excel_uploads")
public class ExcelUploads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "upload_time")
    private LocalDateTime uploadtTime;

    @Lob
    private byte[] file;

    public ExcelUploads(byte[] file) {
        this.file = file;
    }

    public ExcelUploads() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getUploadtTime() {
        return uploadtTime;
    }

    public void setUploadtTime(LocalDateTime uploadtTime) {
        this.uploadtTime = uploadtTime;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

}
