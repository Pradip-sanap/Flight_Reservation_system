package com.flight.service.Impl;

import com.flight.model.Payment;
import com.flight.repository.PaymentRepository;
import com.flight.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment newPaymentCreate(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByPaymentId(Integer paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(()-> new RuntimeException("Payment Not found for paymentId:"+ paymentId ) );
    }
}
