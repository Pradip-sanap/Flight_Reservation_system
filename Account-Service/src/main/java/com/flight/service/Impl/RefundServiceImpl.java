package com.flight.service.Impl;

import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.model.Payment;
import com.flight.model.Refunds;
import com.flight.repository.RefundRepository;
import com.flight.service.AccountService;
import com.flight.service.PaymentService;
import com.flight.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final PaymentService paymentService;
    private final AccountService accountService;

    @Override
    public RefundResponse saveRefundDetails(RefundDto refundDto) {
        //get Payment details
        Payment payment = paymentService.getPaymentByPaymentId(refundDto.getPaymentId());

        Double refundedAmount = refundPolicy(refundDto.getBookingDateTime(), payment);

        Double deposited = accountService.deposit(refundDto.getUserId(), refundedAmount);
        System.out.println("Amount deposited");

        Refunds refunds = mapToEntity(refundDto, refundedAmount);

        Refunds saved = refundRepository.save(refunds);

        return mapToDto(saved);
    }
    private RefundResponse mapToDto(Refunds refunds){
        return new RefundResponse("Refund Successful", refunds.getRefundAmount());
    }
    private Refunds mapToEntity(RefundDto refundDto, double refundedAmount){
        Refunds refunds = new Refunds();
        refunds.setBookingId(refundDto.getBookingId());
        refunds.setPaymentId(refundDto.getPaymentId());
        refunds.setUserId(refundDto.getUserId());
        refunds.setRefundAmount(refundedAmount);
        refunds.setDateTime(LocalDateTime.now());
        return refunds;
    }

    // Refund Policy:
    private Double refundPolicy(LocalDateTime bookingTime, Payment payment){
        double refundedAmount = 0;
        LocalDateTime now = LocalDateTime.now();                        // current time

        // checking whether booking done  within 24 hours
        if (bookingTime.plusHours(24).isAfter(now)) {
            // within 24 hours user will get full refund
            refundedAmount = payment.getPrice();
        } else {
            // after 24 hours user will get  50% refund
            refundedAmount = payment.getPrice() * 0.5;
        }
        return refundedAmount;
    }
}
