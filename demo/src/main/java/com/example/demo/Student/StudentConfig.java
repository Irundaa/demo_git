package com.example.demo.Student;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CommandLinePropertySource;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student mariam = new Student(
                    "Mariam",
                    LocalDate.of(2000, JANUARY, 5),
                    "mariam.jamal@gmail.com"
            );

            Student alex = new Student(
                    "Alex",
                    LocalDate.of(2006, JANUARY, 5),
                    "alex@gmail.com"
            );

            repository.saveAll(
                    List.of(mariam, alex)
            );
        };
    }
}
