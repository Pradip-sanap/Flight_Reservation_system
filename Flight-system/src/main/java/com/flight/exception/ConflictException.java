package com.flight.exception;

class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}