package com.flight.exception;

public class InvalidBookingStateException extends RuntimeException{
    public InvalidBookingStateException(String msg){
        super(msg);
    }
}
