package com.flight.exception;

public class RefundProcessingException extends  RuntimeException{
    public RefundProcessingException(String mesg){
        super(mesg);
    }
}
