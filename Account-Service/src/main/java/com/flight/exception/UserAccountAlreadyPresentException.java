package com.flight.exception;

public class UserAccountAlreadyPresentException extends RuntimeException{
    public UserAccountAlreadyPresentException(String msg){
        super(msg);
    }
}
