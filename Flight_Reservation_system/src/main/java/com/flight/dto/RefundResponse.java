package com.flight.dto;

import com.flight.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundResponse {
    private String status;
    private Double refundAmount;
    private BookingStatus bookingStatus;
    private String message;
}
