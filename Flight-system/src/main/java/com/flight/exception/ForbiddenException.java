package com.flight.exception;

class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}