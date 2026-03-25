package com.flight.repository;

import com.flight.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findById(Integer bookingId);

    // Users all bookings
    List<Booking> findAllByUserId(Integer userId);

    //All bookings for a flight
    List<Booking> findAllByFlightNumber(String flightNumber);
}
