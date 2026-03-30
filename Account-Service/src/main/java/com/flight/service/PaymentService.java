package com.flight.service;

import com.flight.dto.PaymentDto;
import com.flight.model.Payment;

public interface PaymentService {

    PaymentDto newPaymentCreate(PaymentDto payment);
    PaymentDto getPaymentByPaymentId(Integer payment);

}
