package com.example.demo.Student;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
public class StudentDTOO {
    private Long id;
    private String name;
    private LocalDate dob;
    private String email;
    private Integer age;

    public StudentDTOO(Long id, String name, LocalDate dob, String email) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.age = Period.between(this.dob, LocalDate.now()).getYears();
    }

}
