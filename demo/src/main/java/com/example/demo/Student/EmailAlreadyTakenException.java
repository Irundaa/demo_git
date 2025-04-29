package com.example.demo.Student;

public class EmailAlreadyTakenException extends IllegalAccessException{
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
