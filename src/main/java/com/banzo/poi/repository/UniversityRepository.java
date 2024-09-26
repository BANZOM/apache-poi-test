package com.banzo.poi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banzo.poi.entities.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    University findByName(String name);

}