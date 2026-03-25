package com.flight.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String flightNumber) {
        super("Flight not found with flight number: " + flightNumber);
    }
}
