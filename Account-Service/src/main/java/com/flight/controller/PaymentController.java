package com.flight.controller;

import com.flight.dto.PaymentDto;
import com.flight.model.Payment;
import com.flight.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto>  getPaymentById(@PathVariable Integer paymentId){
        log.info("Received request to fetch payment details for paymentId={}", paymentId);
        PaymentDto payment = paymentService.getPaymentByPaymentId(paymentId);
        log.info("Request successfully for paymentId={}", paymentId);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PaymentDto> createNewPayment(@RequestBody PaymentDto paymentDto){
        log.info("Received request to create new payment for bookingId={} amount={}", paymentDto.getBookingId(), paymentDto.getAmount());
        PaymentDto newPayment = paymentService.newPaymentCreate(paymentDto);
        log.info("Request Successful with paymentId={}", newPayment.getPaymentId());
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }


}
