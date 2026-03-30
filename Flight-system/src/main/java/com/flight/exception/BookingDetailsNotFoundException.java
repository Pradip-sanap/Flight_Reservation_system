package com.flight.exception;

public class BookingDetailsNotFoundException extends RuntimeException{
    public BookingDetailsNotFoundException(String msg){
        super(msg);
    }
}
