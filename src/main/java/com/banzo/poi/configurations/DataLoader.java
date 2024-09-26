package com.banzo.poi.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init() {
        return args -> {
            System.out.println("Data loaded");
        };
    }

}
