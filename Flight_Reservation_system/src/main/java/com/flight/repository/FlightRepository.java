package com.flight.repository;

import com.flight.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Optional<Flight> findFlightByFlightNumber(String flightNumber);

    @Modifying
    @Query("UPDATE Flight f SET f.availableSeats = :availableSeats WHERE f.id = :flightId")
    Optional<Integer> updateAvailableSeats(@Param("flightId") Integer flightId, @Param("availableSeats") Integer availableSeats);
}
