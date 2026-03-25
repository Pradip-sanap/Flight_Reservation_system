package com.flight.exception;

public class PaymentUnsuccessfulException extends RuntimeException{
    public PaymentUnsuccessfulException(String mesg){
        super(mesg);
    }
}
