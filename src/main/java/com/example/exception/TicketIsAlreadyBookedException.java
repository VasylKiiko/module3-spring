package com.example.exception;

public class TicketIsAlreadyBookedException extends RuntimeException {
    public TicketIsAlreadyBookedException() {
    }

    public TicketIsAlreadyBookedException(String message) {
        super(message);
    }
}
