package com.flight.controller;

import com.flight.dto.BookingDto;
import com.flight.dto.BookingResponse;
import com.flight.dto.PaymentDto;
import com.flight.dto.RefundResponse;
import com.flight.model.Booking;
import com.flight.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingResponse> bookFlight(
//            @RequestParam Integer userId,
//            @RequestParam String flightNumber,
//            @RequestParam Integer seatCount
            @RequestBody BookingDto bookingDto
            )
    {
        BookingResponse resp = bookingService.bookFlight(bookingDto.getUserId(), bookingDto.getFlightNumber(), bookingDto.getSeatCount());
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Booking>> getUsersBookings(@PathVariable Integer userId){
        List<Booking> usersBookings = bookingService.getUsersBookings(userId);
        return new ResponseEntity<>(usersBookings, HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<Object> doPayment(@RequestBody PaymentDto paymentDto){
        return new ResponseEntity<>(bookingService.doPayment(paymentDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<RefundResponse> cancelFlightBooking(@PathVariable Integer bookingId){
        RefundResponse resp = bookingService.cancelFlightBooking(bookingId);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
