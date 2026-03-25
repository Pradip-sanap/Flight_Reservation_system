package com.flight.service;

import com.flight.model.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(value = "payment-service", url = "http://localhost:8090/account/payment")
public interface PaymentServiceFeignClient {

    @GetMapping("/{paymentId}")
    Optional<Payment> getPaymentById(@PathVariable("paymentId") Integer payId);

    @PostMapping()
    Payment createNewPayment(@RequestBody Payment payment);
}
