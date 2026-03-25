package com.flight.dto;

import com.flight.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingCancelResponse {

    private Integer bookingId;
    private BookingStatus status;
    private String message;
    private String refund;

}
