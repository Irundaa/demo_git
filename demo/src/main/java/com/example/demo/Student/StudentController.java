package com.example.demo.Student;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public Optional<StudentDTO> findStudentById(@PathVariable Long studentId) {
        return studentService.findStudentById(studentId);
    }

    @PostMapping
    public void registerNewStudent(@RequestBody StudentDTO student) throws IllegalAccessException {
        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId){
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) throws EmailAlreadyTakenException {
        studentService.updateStudent(studentId, name, email);
    }

    @GetMapping
    public List<StudentDTO> getStudents(){
        return studentService.getStudents();
    }

}
