package com.example.exception;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException() {
    }

    public EventAlreadyExistsException(String message) {
        super(message);
    }
}
