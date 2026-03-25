package com.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Integer paymentId;
    private Double paymentAmount;
    private String message;

}
