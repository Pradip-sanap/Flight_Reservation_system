package com.flight.exception;

public class RefundServiceDownError extends RuntimeException {
    public RefundServiceDownError(String message) {
        super(message);
    }
    public RefundServiceDownError(String message, Throwable ex) {
        super(message, ex);
    }
}
