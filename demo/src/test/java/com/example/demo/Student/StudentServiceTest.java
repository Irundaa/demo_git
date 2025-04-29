package com.example.demo.Student;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test(expected = IllegalAccessException.class)
    public void addNewStudentThrowsException() throws IllegalAccessException {
        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(true);

        studentService.addNewStudent(studentDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteStudentThrowsException() throws IllegalArgumentException {
        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.existsById(studentDTO.getId())).thenReturn(false);

        studentService.deleteStudent(studentDTO.getId());
    }

    @Test
    public void updateStudent() throws EmailAlreadyTakenException {
//        Student student = mock(Student.class);
        Student student = new Student();
        when(studentRepository.findById(10L)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail("Mary23@gmail.com")).thenReturn(Optional.empty());


        studentService.updateStudent(10L, "Mary Jane", "Mary23@gmail.com");

        assertEquals("Mary Jane", student.getName());
        assertEquals("Mary23@gmail.com", student.getEmail());

    }

    @Test
    public void findStudentById() {
    }

    @Test
    public void getStudents() {
    }
}