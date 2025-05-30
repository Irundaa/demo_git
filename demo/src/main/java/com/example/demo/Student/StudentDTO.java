package com.example.demo.Student;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Data
@NoArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private Optional<LocalDate> dob; //optional
    private String email;
    private Integer age;

    public StudentDTO(Long id, String name, Optional<LocalDate> dob, String email) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.age = dob
                .map(date -> Period.between(date, LocalDate.now()).getYears())
                .orElse(null);
    }

}
