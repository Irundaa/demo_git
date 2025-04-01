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

    public void addNewStudent(StudentDTOO studentDTOO) throws IllegalAccessException {
//        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
//        if (studentOptional.isPresent()) {
//            throw new IllegalAccessException("email taken");
//        }
        Student student = new Student();
        student.setEmail(studentDTOO.getEmail());
        student.setName(studentDTOO.getName());
        student.setDob(studentDTOO.getDob());
        if (studentRepository.selectExistsEmail(studentDTOO.getEmail())) {
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
    public Optional<StudentDTOO> findStudentById(Long studentId) {
        Optional<Student> fromDB = studentRepository.findById(studentId);
        Student student = fromDB.get();
        return Optional.of(convert(student));
    }

    public List<StudentDTOO> getStudents(){
        List<Student> students = studentRepository.findAll();
        List<StudentDTOO> studentsDTO = new ArrayList<>();

        for (Student student : students) {
            studentsDTO.add(convert(student));
        }
        return studentsDTO;
    }

    private StudentDTOO convert(Student student) {
        StudentDTOO studentDTOO = new StudentDTOO();
        studentDTOO.setId(student.getId());
        studentDTOO.setName(student.getName());
        studentDTOO.setEmail(student.getEmail());
        studentDTOO.setDob(student.getDob());
        studentDTOO.setAge(student.getAge());
        return studentDTOO;
    }
}
