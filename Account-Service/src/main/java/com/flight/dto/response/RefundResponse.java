package com.flight.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundResponse {
    private String status;
    private Double refundAmount;
}
