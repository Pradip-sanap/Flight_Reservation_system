package com.flight.dto;

import lombok.Data;

@Data
public class BookingDto {
    private Integer  userId;
    private String  flightNumber;
    private Integer  seatCount;
}
