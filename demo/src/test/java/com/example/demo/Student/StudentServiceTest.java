package com.example.demo.Student;

import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentServiceTest {

    private StudentRepository studentRepository;
    private StudentService studentService;

    @Test(expected = IllegalAccessException.class)
    public void addNewStudentThrowsException() throws IllegalAccessException {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);

        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(true);

        studentService.addNewStudent(studentDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteStudentThrowsException() throws IllegalArgumentException {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);

        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.existsById(studentDTO.getId())).thenReturn(false);

        studentService.deleteStudent(studentDTO.getId());
    }

    @Test
    public void updateStudent() {
    }

    @Test
    public void findStudentById() {
    }

    @Test
    public void getStudents() {
    }
}