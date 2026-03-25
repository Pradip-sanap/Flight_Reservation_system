package com.flight.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private Integer bookingId;
    private String message;
    private Double price;
    private Integer totalBookSeats;
}
