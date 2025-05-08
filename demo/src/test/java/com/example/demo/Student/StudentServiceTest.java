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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private static final Long ID = 10L;
    private static final String NAME = "Mary Jane";
    private static final LocalDate DOB = LocalDate.of(2002, 1, 1);
    private static final String EMAIL = "mary@gmail.com";
    private StudentDTO studentDTO = new StudentDTO(1L, NAME, DOB, EMAIL);

    @Test
    public void addNewStudentThrowsException(){
        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(true);

        try {
            studentService.addNewStudent(studentDTO);
        } catch (EmailAlreadyTakenException e) {
            assertEquals(String.format("email %s is taken", studentDTO.getEmail()), e.getMessage());
        }
    }

    @Test
    public void addNewStudent() throws EmailAlreadyTakenException {
        Student student = new Student(NAME, DOB, EMAIL);
        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(false);

        studentService.addNewStudent(studentDTO);

        verify(studentRepository).save(student);
    }

    @Test
    public void deleteStudentThrowsException() {
        when(studentRepository.existsById(studentDTO.getId())).thenReturn(false);

        try {
            studentService.deleteStudent(studentDTO.getId());
        } catch (IllegalArgumentException e) {
            assertEquals(String.format("Student with id %s does not exists", studentDTO.getId()), e.getMessage());
        }
    }

    @Test
    public void deleteStudent() {
        when(studentRepository.existsById(ID)).thenReturn(true);

        studentService.deleteStudent(ID);

        verify(studentRepository).deleteById(ID);
    }

    @Test
    public void updateStudentEmailAndName() throws EmailAlreadyTakenException {
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail(EMAIL)).thenReturn(Optional.empty());

        studentService.updateStudent(ID, NAME, EMAIL);

        assertEquals(NAME, student.getName());
        assertEquals(EMAIL, student.getEmail());
    }

    @Test
    public void updateStudentName() throws EmailAlreadyTakenException {
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail(EMAIL)).thenReturn(Optional.empty());

        studentService.updateStudent(ID, NAME, null);

        assertEquals(NAME, student.getName());
    }

    @Test
    public void updateStudentEmail() throws EmailAlreadyTakenException {
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail(EMAIL)).thenReturn(Optional.empty());

        studentService.updateStudent(ID, null, EMAIL);

        assertEquals(EMAIL, student.getEmail());
    }

    @Test
    public void updateStudentThrowsStudentWithIdDoesNotExistException() {
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        try {
            studentService.updateStudent(ID, NAME, EMAIL);
        } catch (IllegalArgumentException e) {
            assertEquals(String.format("Student with id %s does not exists", ID), e.getMessage());
        }
    }

    @Test
    public void updateStudentThrowsNameAndEmailCannotBeNullException(){
        Student student = new Student();

        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));

        try {
            studentService.updateStudent(ID, null, null);

        } catch (IllegalArgumentException e) {
            assertEquals("Name and/or email must be provided and different from existing values", e.getMessage());
        }
    }

    @Test
    public void updateStudentThrowsEmailAlreadyTakenException() {
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));
        when(studentRepository.findStudentByEmail(EMAIL)).thenReturn(Optional.of(student));

        try {
            studentService.updateStudent(ID, NAME, EMAIL);
        } catch (EmailAlreadyTakenException e) {
            assertEquals(String.format("email %s is taken", EMAIL), e.getMessage());
        }
    }

    @Test
    public void findStudentById() {
        Optional<Student> student = Optional.of(new Student(NAME, DOB, EMAIL));

        when(studentRepository.findById(ID)).thenReturn(student);

        Optional<StudentDTO> result = studentService.findStudentById(ID);

        assertTrue(result.isPresent());
        StudentDTO dto = result.get();
        assertEquals(NAME, dto.getName());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(DOB, dto.getDob());
    }

    @Test
    public void getStudents() {
        Student student1 = new Student(NAME, DOB, EMAIL);
        Student student2 = new Student("Lion King", LocalDate.of(2005, 1, 1), "Lion20@gmail.com");

        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getStudents();

        assertEquals(2, result.size());
        assertEquals(NAME, result.get(0).getName());
        assertEquals(EMAIL, result.get(0).getEmail());
        assertEquals(DOB, result.get(0).getDob());
        assertEquals("Lion King", result.get(1).getName());
        assertEquals("Lion20@gmail.com", result.get(1).getEmail());
        assertEquals(LocalDate.of(2005, 1, 1), result.get(1).getDob());
    }
}