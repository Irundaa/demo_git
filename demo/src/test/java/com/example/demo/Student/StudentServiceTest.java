package com.example.demo.Student;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void addNewStudent() throws IllegalAccessException {
        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(false);

        studentService.addNewStudent(studentDTO);

        verify(studentRepository).save(any(Student.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteStudentThrowsException() throws IllegalArgumentException {
        StudentDTO studentDTO = new StudentDTO(1L,"Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");

        when(studentRepository.existsById(studentDTO.getId())).thenReturn(false);

        studentService.deleteStudent(studentDTO.getId());
    }

    @Test
    public void deleteStudent() throws IllegalAccessException {
        when(studentRepository.existsById(10L)).thenReturn(true);

        studentService.deleteStudent(10L);

        verify(studentRepository).deleteById(10L);
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

    @Test(expected = IllegalArgumentException.class)
    public void updateStudentThrowsStudentWithIdDoesNotExistException() throws IllegalArgumentException, EmailAlreadyTakenException {
        when(studentRepository.findById(10L)).thenReturn(Optional.empty());

        studentService.updateStudent(10L, "Mary Jane", "Mary23@gmail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateStudentThrowsNameCannotBeNullException() throws IllegalArgumentException, EmailAlreadyTakenException {
        Student student = new Student();

        when(studentRepository.findById(10L)).thenReturn(Optional.ofNullable(student));

        studentService.updateStudent(10L, null, "Mary23@gmail.com");
    }

    @Test(expected = EmailAlreadyTakenException.class)
    public void updateStudentThrowsEmailAlreadyTakenException() throws IllegalArgumentException, EmailAlreadyTakenException {
        Student student = new Student();

        when(studentRepository.findById(10L)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail("Mary23@gmail.com")).thenReturn(Optional.ofNullable(student));

        studentService.updateStudent(10L, "Mary Jane", "Mary23@gmail.com");
    }

    @Test
    public void findStudentById() {
        Optional<Student> student = Optional.of(new Student("Mary Jane", LocalDate.of(2002, 1, 1), "Mary23@gmail.com"));

        when(studentRepository.findById(10L)).thenReturn(student);

        Optional<StudentDTO> result = studentService.findStudentById(10L);

        assertEquals("Mary Jane", result.get().getName());
        assertEquals("Mary23@gmail.com", result.get().getEmail());
        assertEquals(LocalDate.of(2002, 1, 1), result.get().getDob());
    }

    @Test
    public void getStudents() {
        Student student1 = new Student("Mary Jane", LocalDate.of(2002,1,1), "Mary23@gmail.com");
        Student student2 = new Student("Lion King", LocalDate.of(2005,1,1), "Lion20@gmail.com");

        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getStudents();

        assertEquals(2, result.size());
        assertEquals("Mary Jane", result.get(0).getName());
        assertEquals("Mary23@gmail.com", result.get(0).getEmail());
        assertEquals(LocalDate.of(2002, 1, 1), result.get(0).getDob());
        assertEquals("Lion King", result.get(1).getName());
        assertEquals("Lion20@gmail.com", result.get(1).getEmail());
        assertEquals(LocalDate.of(2005, 1, 1), result.get(1).getDob());
    }
}