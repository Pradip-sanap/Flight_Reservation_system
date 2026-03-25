package com.flight.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer  userId;
    private String  flightNumber;
    private Integer  seatCount;
}
