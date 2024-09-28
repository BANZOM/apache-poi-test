package com.banzo.poi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banzo.poi.entities.ExcelUploads;

@Repository
public interface ExcelUploadsRepository extends JpaRepository<ExcelUploads, Long> {

}
