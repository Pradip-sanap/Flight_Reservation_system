package com.flight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refunds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refundId;
    private Integer bookingId;
    private Integer paymentId;
    private Integer userId;
    private Double amount;
    private LocalDateTime dateTime;
}
