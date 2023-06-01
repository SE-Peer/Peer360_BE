package com.example.peer360.handler;

public class UserAlreadyParticipatingException extends RuntimeException {
    public UserAlreadyParticipatingException(String message) {
        super(message);
    }
}