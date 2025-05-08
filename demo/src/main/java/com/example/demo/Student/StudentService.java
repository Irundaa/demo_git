package com.example.demo.Student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addNewStudent(StudentDTO studentDTO) throws EmailAlreadyTakenException {
        Student student = new Student();
        student.setEmail(studentDTO.getEmail());
        student.setName(studentDTO.getName());
        student.setDob(studentDTO.getDob());
        if (studentRepository.selectExistsEmail(studentDTO.getEmail())) {
            throw new EmailAlreadyTakenException(String.format("email %s is taken", student.getEmail())); //написати який саме тейкен
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new StudentWithIdDoesNotExistException(String.format("Student with id %s does not exists", studentId)); //do the same format
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) throws IllegalArgumentException{
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentWithIdDoesNotExistException(
                        String.format("Student with id %s does not exists", studentId))); //do

        boolean isUpdated = false;

        if (name != null && !name.trim().isEmpty() && !Objects.equals(name, student.getName())) {
            student.setName(name);
            isUpdated = true;
        }

        if (email != null && !email.trim().isEmpty() && isEmailFree(email, student)) {
            student.setEmail(email);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("Name and/or email must be provided and different from existing values");
        }
    }//розглянути 4 варіанти для оновлення студента в окремому методі

    public boolean isEmailFree(String email, Student student) throws EmailAlreadyTakenException {
        if (!StringUtils.isEmpty(email) &&
                !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new EmailAlreadyTakenException("email taken");
            }
        }
        return true;
    }

    public Optional<StudentDTO> findStudentById(Long studentId) {
        Optional<Student> fromDB = studentRepository.findById(studentId);
        Student student = fromDB.get();
        return Optional.of(convert(student));
    }

    public List<StudentDTO> getStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentsDTO = new ArrayList<>();

        for (Student student : students) {
            studentsDTO.add(convert(student));
        }
        return studentsDTO;
    }

    private StudentDTO convert(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setDob(student.getDob());
        studentDTO.setAge(student.getAge());
        return studentDTO;
    }
}
