package com.banzo.poi.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.banzo.poi.entities.Student;
import com.banzo.poi.entities.University;
import com.banzo.poi.repository.StudentRepository;
import com.banzo.poi.repository.UniversityRepository;

@Configuration
public class DataLoader {

    @Bean
    @Order(1)
    CommandLineRunner saveStudentsDummyData(StudentRepository studentRepository,
            UniversityRepository universityRepository) {
        return args -> {
            universityRepository.save(new University("University of Banzo", "Banzo Street, 123", "123456789"));
            universityRepository.save(new University("University of Poi", "Poi Street, 456", "987654321"));
            universityRepository.save(new University("University of Foo", "Foo Street, 789", "456123789"));

            studentRepository.save(new Student("John Doe", "john@gmail.com", "123456789",
                    universityRepository.findByName("University of Banzo")));
            studentRepository.save(new Student("Aditya", "aditya@banzo.tech", "987654321",
                    universityRepository.findByName("University of Banzo")));
            
            studentRepository.save(new Student("Jane Doe", "jane@dogs.com", "456123789",
                    universityRepository.findByName("University of Poi")));
            studentRepository.save(new Student("Foo Bar", "foo@bar.com", "789456123",
                    universityRepository.findByName("University of Poi")));

            studentRepository.save(new Student("Alice", "mate@alice.co", "321654987",
                    universityRepository.findByName("University of Foo")));
            studentRepository.save(new Student("Bob", "bob@rob.com", "654987321",
                    universityRepository.findByName("University of Foo")));

            System.out.println("Data loaded successfully!");
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner printStudents(StudentRepository studentRepository) {
        return args -> {
            System.out.println("Students:");
            studentRepository.findAll().forEach(System.out::println);
        };
    }

}
