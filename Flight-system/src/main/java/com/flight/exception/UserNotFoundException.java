package com.flight.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, Throwable stack) {
        super(message, stack);
    }
}
