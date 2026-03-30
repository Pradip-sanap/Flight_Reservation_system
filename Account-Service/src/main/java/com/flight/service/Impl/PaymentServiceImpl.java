package com.flight.service.Impl;

import com.flight.dto.PaymentDto;
import com.flight.exception.PaymentNotFoundException;
import com.flight.model.Payment;
import com.flight.repository.PaymentRepository;
import com.flight.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto newPaymentCreate(PaymentDto paymentDto) {
        log.info("Request received for new payment for bookingId={} and amount={}", paymentDto.getBookingId(), paymentDto.getAmount());
        Payment payment = mapToEntity(paymentDto);
        Payment saved = paymentRepository.save(payment);
        log.info("Payment successfully created with paymentId={}", saved.getPaymentId());
        return mapToDto(saved);
    }

    @Override
    public PaymentDto getPaymentByPaymentId(Integer paymentId) {
        log.info("Fetching payment details for paymentId={}", paymentId);
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->{
            log.error("Payment not found for paymentId={}", paymentId);
            return new PaymentNotFoundException("Payment not found");
        });
        log.info("Payment retrieved successfully for paymentId={}", paymentId);
        return mapToDto(payment);
    }

    private Payment mapToEntity(PaymentDto dto){
        return new Payment(dto.getBookingId(), dto.getAmount(), dto.getDateTime());
    }

    private PaymentDto mapToDto(Payment payment){
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setBookingId(payment.getBookingId());
        dto.setDateTime(payment.getDateTime());
        return dto;
    }
}
