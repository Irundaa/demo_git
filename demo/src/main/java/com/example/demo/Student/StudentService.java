package com.example.demo.Student;

import jakarta.transaction.Transactional;
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

//    @Autowired
//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }

    public void addNewStudent(StudentDTO studentDTO) throws IllegalAccessException {
//        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
//        if (studentOptional.isPresent()) {
//            throw new IllegalAccessException("email taken");
//        }
        Student student = new Student();
        student.setEmail(studentDTO.getEmail());
        student.setName(studentDTO.getName());
        student.setDob(studentDTO.getDob());
        if (studentRepository.selectExistsEmail(studentDTO.getEmail())) {
            throw new IllegalAccessException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalArgumentException("Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student with id " + studentId + " does not exists"));

        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(name, student.getName())) {
            student.setName(name);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalArgumentException("email taken");
            }
            student.setEmail(email);
        }
    }
    public Optional<StudentDTO> findStudentById(Long studentId) {
        Optional<Student> fromDB = studentRepository.findById(studentId);
        Student student = fromDB.get();
        return Optional.of(convert(student));
    }

    public List<StudentDTO> getStudents(){
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
