package com.flight.service.impl;

import com.flight.exception.FlightNotFoundException;
import com.flight.model.Flight;
import com.flight.repository.FlightRepository;
import com.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;


    @Override
    public Flight addFlight(Flight flight) {
        log.debug("Flight details saving in database");
        return flightRepository.save(flight);
    }

    @Override
    public Flight getFlight(int flightId) {
        return flightRepository.findById(flightId).orElseThrow(() ->{
            log.error("Flight details not found for flightId={}", flightId);
            return new RuntimeException("Flight not found with id: " + flightId);
        });
    }

    @Override
    public Flight getFlightByFlightNumber(String flightNumber) {
        return flightRepository.findFlightByFlightNumber(flightNumber).orElseThrow(()->{
            log.error("Flight details not found for flightNumber={}", flightNumber);
            return new FlightNotFoundException(flightNumber);
        });
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public int updateAvailableSeats(Integer flightId, Integer availableSeats) {
        return flightRepository.updateAvailableSeats(flightId, availableSeats).orElseThrow(()-> {
            log.error("Flight details not found for flightId={}", flightId);
            return new FlightNotFoundException("Flight details not found");
        });
    }
}
