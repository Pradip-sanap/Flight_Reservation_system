package com.flight.service.impl;

import com.flight.exception.FlightNotFoundException;
import com.flight.model.Flight;
import com.flight.repository.FlightRepository;
import com.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributeValueException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;


    @Override
    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight getFlight(int flightId) {
        return flightRepository.findById(flightId).orElseThrow(() ->
                                                                new RuntimeException("Flight not found with id: " + flightId));
    }

    @Override
    public Flight getFlightByFlightNumber(String flightNumber) {
        return flightRepository.findFlightByFlightNumber(flightNumber).orElseThrow(()->
                                                                    new FlightNotFoundException(flightNumber));
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public int updateAvailableSeats(Integer flightId, Integer availableSeats) {
        return flightRepository.updateAvailableSeats(flightId, availableSeats).orElseThrow(()->
                                                                                new RuntimeException("Something went wrong"));
    }
}
