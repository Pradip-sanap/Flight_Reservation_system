package com.flight.service;

import com.flight.dto.response.BookingResponse;
import com.flight.dto.PaymentDto;
import com.flight.dto.response.RefundResponse;
import com.flight.model.Booking;

import java.util.List;

public interface BookingService {

    BookingResponse bookFlight(Integer userId, String flightNumber, Integer seatCount);

    Booking getBookingDetailById(Integer bookingId);

    List<Booking> getUsersBookings(Integer userId);

    Object doPayment(PaymentDto paymentDto);

    RefundResponse cancelFlightBooking(Integer bookingId);

}
