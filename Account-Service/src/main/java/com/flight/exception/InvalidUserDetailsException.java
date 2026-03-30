package com.flight.exception;

public class InvalidUserDetailsException extends RuntimeException{
    public InvalidUserDetailsException(String msg){
        super(msg);
    }
}
