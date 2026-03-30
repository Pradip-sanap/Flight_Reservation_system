package com.flight.dto.response;

import com.flight.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundResponse {
    private String status;
    private Double refundAmount;
    private BookingStatus bookingStatus;
    private String message;
}
