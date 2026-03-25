package com.flight.repository;

import com.flight.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

//    Optional<Payment> findByBookingId(Integer paymentId);
}
