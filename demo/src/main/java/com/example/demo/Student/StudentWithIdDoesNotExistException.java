package com.example.demo.Student;

public class StudentWithIdDoesNotExistException extends IllegalArgumentException{
    public StudentWithIdDoesNotExistException(String message) {
        super(message);
    }
}
