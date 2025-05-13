package com.example.demo.Student;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //отримувати студента дто а не окремі атрибути так само в контролері
    @Transactional
    public void updateStudent(StudentDTO studentDTO, Long studentId) throws IllegalArgumentException{
        Student studentDB = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentWithIdDoesNotExistException(
                        "Student with id " + studentId + " does not exists"));
        String name = studentDTO.getName();
        String email = studentDTO.getEmail();

        boolean isUpdated = false;

        if (StringUtils.isNotBlank(name) && !Objects.equals(name, studentDB.getName())) {
            studentDB.setName(name);
            isUpdated = true;
        }

        if (isEmailFree(email, studentDB)) {
            studentDB.setEmail(email);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("Name and/or email must be provided and different from existing values");
        }
    }

    public boolean isEmailFree(String email, Student student) throws EmailAlreadyTakenException {
        if (StringUtils.isNotBlank(email) &&
                !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new EmailAlreadyTakenException(String.format("email %s is taken", email));
            }
            return true;
        }
        return false;
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
