package com.flight.exception;

public class InvalidRefreshTokenException extends RuntimeException{

    public InvalidRefreshTokenException(String mesg){
        super(mesg);
    }
}
