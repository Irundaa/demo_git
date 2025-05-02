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

    private StudentDTO studentDTO = new StudentDTO(1L, "Mary Jane", LocalDate.of(2002, 1, 1), "Mary23@gmail.com");
    private static final Long ID = 10L;
    //повиносити змінні

    @Test(expected = EmailAlreadyTakenException.class)
    public void addNewStudentThrowsException() throws EmailAlreadyTakenException {
        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(true);

        studentService.addNewStudent(studentDTO);
    }

    @Test
    public void addNewStudent() throws EmailAlreadyTakenException {
        Student student = new Student("Mary Jane", LocalDate.of(2002, 1, 1), "Mary23@gmail.com");
        when(studentRepository.selectExistsEmail(studentDTO.getEmail())).thenReturn(false);

        studentService.addNewStudent(studentDTO);

        verify(studentRepository).save(student);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteStudentThrowsException() throws IllegalArgumentException {
        when(studentRepository.existsById(studentDTO.getId())).thenReturn(false);

        studentService.deleteStudent(studentDTO.getId());
    }

    @Test
    public void deleteStudent() {
        when(studentRepository.existsById(ID)).thenReturn(true);

        studentService.deleteStudent(ID);

        verify(studentRepository).deleteById(ID);
    }

    @Test
    public void updateStudent() throws EmailAlreadyTakenException {
//        Student student = mock(Student.class);
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.ofNullable(student));
        when(studentRepository.findStudentByEmail("Mary23@gmail.com")).thenReturn(Optional.empty());

        studentService.updateStudent(ID, "Mary Jane", "Mary23@gmail.com");

        assertEquals("Mary Jane", student.getName());
        assertEquals("Mary23@gmail.com", student.getEmail());
    }//протестити всі кейси

    @Test
    public void updateStudentThrowsStudentWithIdDoesNotExistException() {
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        try {
            studentService.updateStudent(ID, "Mary Jane", "Mary23@gmail.com");
        } catch (IllegalArgumentException e) {
            assertEquals(String.format("Student with id %s does not exists", ID), e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateStudentThrowsNameCannotBeNullException() throws IllegalArgumentException {
        Student student = new Student();

        when(studentRepository.findById(ID)).thenReturn(Optional.ofNullable(student));

        studentService.updateStudent(ID, null, "Mary23@gmail.com");
    }

    @Test(expected = EmailAlreadyTakenException.class)
    public void updateStudentThrowsEmailAlreadyTakenException() throws EmailAlreadyTakenException {
        Student student = new Student();
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));
        when(studentRepository.findStudentByEmail("Mary23@gmail.com")).thenReturn(Optional.of(student));

        studentService.updateStudent(ID, "Mary Jane", "Mary23@gmail.com");
    }

    @Test
    public void findStudentById() {
        Optional<Student> student = Optional.of(new Student("Mary Jane", LocalDate.of(2002, 1, 1), "Mary23@gmail.com"));

        when(studentRepository.findById(ID)).thenReturn(student);

        Optional<StudentDTO> result = studentService.findStudentById(ID);

        assertTrue(result.isPresent());
        StudentDTO dto = result.get();
        assertEquals("Mary Jane", dto.getName());
        assertEquals("Mary23@gmail.com", dto.getEmail());
        assertEquals(LocalDate.of(2002, 1, 1), dto.getDob());
    }

    @Test
    public void getStudents() {
        Student student1 = new Student("Mary Jane", LocalDate.of(2002, 1, 1), "Mary23@gmail.com");
        Student student2 = new Student("Lion King", LocalDate.of(2005, 1, 1), "Lion20@gmail.com");

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