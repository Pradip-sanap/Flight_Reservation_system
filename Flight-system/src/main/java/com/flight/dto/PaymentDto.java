package com.flight.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Integer paymentId;
    private Integer bookingId;
    private Double amount;
    private LocalDateTime dateTime;
    public PaymentDto(Integer bookingId, Double amount, LocalDateTime now) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.dateTime = now;
    }
}
