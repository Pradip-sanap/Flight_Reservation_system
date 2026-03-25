package com.flight.service;

import com.flight.model.Flight;

import java.util.List;

public interface FlightService {

    Flight addFlight(Flight flight);

    Flight getFlight(int flightId);

    Flight getFlightByFlightNumber(String flightNumber);

    List<Flight> getAllFlights();

    int updateAvailableSeats(Integer flightId, Integer availableSeats);
}
