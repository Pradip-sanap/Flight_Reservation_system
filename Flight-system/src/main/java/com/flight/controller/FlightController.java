package com.flight.controller;

import com.flight.model.Flight;
import com.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping()
    public ResponseEntity<Flight> AddFlight(@RequestBody Flight flight){
        Flight savedFlight = flightService.addFlight(flight);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable int id){
        Flight flight = flightService.getFlight(id);
        return ResponseEntity.ok(flight);
    }

    @GetMapping()
    public ResponseEntity<List<Flight>> getAllFlights(){
        List<Flight> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flight-number/{flightNumber}")
    public ResponseEntity<Integer> validateFlightNumberAndGetAvailableSeats(@PathVariable String flightNumber){
        Flight flight = flightService.getFlightByFlightNumber(flightNumber);
        return ResponseEntity.ok(flight.getAvailableSeats());
    }
}
