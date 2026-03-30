package com.flight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;
    private Integer bookingId;
    private Double amount;
    private LocalDateTime dateTime;

    public Payment(Integer bookingId, Double amount, LocalDateTime now) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.dateTime = now;
    }


}
