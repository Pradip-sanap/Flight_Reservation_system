package com.flight.model;

import com.flight.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bookings")
public class Booking
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int bookingId;
    private Integer  userId;
    private String  flightNumber;
    private Integer  seatCount;
    private LocalDateTime bookingDateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private Integer paymentId;
}
