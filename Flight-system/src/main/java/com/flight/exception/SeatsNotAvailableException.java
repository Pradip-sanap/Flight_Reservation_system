package com.flight.exception;

public class SeatsNotAvailableException extends RuntimeException{
    public SeatsNotAvailableException(String flightNumber) {
        super("No seats available for flight: " + flightNumber);
    }
}
