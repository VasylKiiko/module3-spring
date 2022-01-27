package com.example.exception;

public class AccountAlreadyRegisteredException extends RuntimeException {
    public AccountAlreadyRegisteredException() {
    }

    public AccountAlreadyRegisteredException(String message) {
        super(message);
    }
}
