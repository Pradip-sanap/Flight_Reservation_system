package com.flight.service.Impl;

import com.flight.dto.PaymentDto;
import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.exception.AccountNotFoundException;
import com.flight.exception.PaymentNotFoundException;
import com.flight.exception.RefundProcessingException;
import com.flight.model.Payment;
import com.flight.model.Refunds;
import com.flight.repository.RefundRepository;
import com.flight.service.AccountService;
import com.flight.service.PaymentService;
import com.flight.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final PaymentService paymentService;
    private final AccountService accountService;

    @Override
    public RefundResponse doRefund(RefundDto refundDto) {
        log.info("Processing refund request for paymentId={} userId={}", refundDto.getPaymentId(), refundDto.getUserId());
        try {
            //get Payment details
            PaymentDto payment = paymentService.getPaymentByPaymentId(refundDto.getPaymentId());
            log.debug("Payment fetched successfully: paymentId={} amount={}", payment.getPaymentId(), payment.getAmount());

            Double refundedAmount = refundPolicy(refundDto.getBookingDateTime(), payment.getAmount());
            log.debug("Calculated refund amount={} for bookingId={}", refundedAmount, refundDto.getBookingId());

            Double deposited = accountService.deposit(refundDto.getUserId(), refundedAmount);
            log.info("Refund amount deposited successfully for userId={} new balance={}", refundDto.getUserId(), deposited);

            Refunds refund = mapToEntity(refundDto, refundedAmount);

            Refunds saved = saveRefund(refund);
            log.info("Refund record saved successfully for paymentId={} refundAmount={}", saved.getPaymentId(), saved.getRefundAmount());
            return mapToDto(saved);

        } catch (PaymentNotFoundException ex) {
            log.error("Payment not found for paymentId={}", refundDto.getPaymentId(), ex);
            throw new RefundProcessingException("Refund could not be processed due to a payment issue. Please verify your payment details.");
        } catch (AccountNotFoundException ex) {
            log.error("Bank account not found for userId={}", refundDto.getUserId(), ex);
            throw new RefundProcessingException("Bank Account not found for user. Please verify details.");
        } catch (Exception ex) {
            log.error("Unexpected error while processing refund for paymentId={} userId={}", refundDto.getPaymentId(), refundDto.getUserId(), ex);
            throw new RefundProcessingException("An unexpected error occurred while processing the refund.");
        }
    }

    private Double refundPolicy(LocalDateTime bookingTime, Double paymentAmount) {
        boolean isBookingWithin24Hour = bookingTime.plusHours(24).isAfter(LocalDateTime.now());
        log.debug("Is booking within 24 hours [yes/No]={}", isBookingWithin24Hour?"yes":"No");
        return isBookingWithin24Hour ?  paymentAmount : paymentAmount * 0.5;
    }

    public Refunds saveRefund(Refunds refund) {
        return refundRepository.save(refund);
    }

    private RefundResponse mapToDto(Refunds refunds) {
        return new RefundResponse("Refund Successful", refunds.getRefundAmount());
    }

    private Refunds mapToEntity(RefundDto refundDto, double refundedAmount) {
        Refunds refunds = new Refunds();
        refunds.setBookingId(refundDto.getBookingId());
        refunds.setPaymentId(refundDto.getPaymentId());
        refunds.setUserId(refundDto.getUserId());
        refunds.setRefundAmount(refundedAmount);
        refunds.setDateTime(LocalDateTime.now());
        return refunds;
    }


}
