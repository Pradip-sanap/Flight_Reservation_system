package com.flight.controller;

import com.flight.model.Payment;
import com.flight.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment>  getPaymentById(@PathVariable Integer paymentId){
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Payment> createNewPayment(@RequestBody Payment payment){
        Payment newPayment = paymentService.newPaymentCreate(payment);
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }


}
