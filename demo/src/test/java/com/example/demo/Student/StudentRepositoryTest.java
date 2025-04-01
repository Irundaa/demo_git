package com.example.demo.Student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void selectExistsEmail() {
        // given
        Student student = new Student(
                "Jamila",
                LocalDate.of(2000, JANUARY, 5),
                "jamila@gmail.com"
        );
        underTest.save(student);
        //when
        Boolean exists = underTest.selectExistsEmail(student.getEmail());
        //then
        assertThat(exists).isTrue();
    }
}