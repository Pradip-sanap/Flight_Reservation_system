package com.flight.service;

import com.flight.model.Payment;

public interface PaymentService {

    Payment newPaymentCreate(Payment payment);
    Payment getPaymentByPaymentId(Integer payment);

}
