package com.example.demo.Student;

public class EmailAlreadyTakenException extends IllegalArgumentException{
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
