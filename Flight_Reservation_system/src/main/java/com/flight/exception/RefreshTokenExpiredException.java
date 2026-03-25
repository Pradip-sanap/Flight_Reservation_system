package com.flight.exception;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String mesg){
        super(mesg);
    }
}
