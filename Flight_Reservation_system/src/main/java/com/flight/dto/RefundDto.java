package com.flight.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundDto {
    private Integer bookingId;
    private Integer userId;
    private Integer paymentId;
    private LocalDateTime bookingDateTime;
}
