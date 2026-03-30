package com.flight.service.impl;

import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.exception.RefundServiceDownError;
import com.flight.service.feignClients.RefundServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundRetryService {

    private final RefundServiceFeignClient refundServiceFeignClient;

    @Retryable(
            retryFor = { RefundServiceDownError.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 4000))
    public RefundResponse processRefund(RefundDto refundDto) {

        RetryContext context = RetrySynchronizationManager.getContext();
        int retryCount = (context != null) ? context.getRetryCount() : 0;
        log.debug("Retry attempt: " + (retryCount + 1));

        try {
            return refundServiceFeignClient.doRefund(refundDto);

        } catch (Exception ex) {   // 👈 catch Feign exception here
            log.error("Exception from refund service: ", ex);
            throw new RefundServiceDownError("Refund service is unavailable", ex);
        }
    }

    @Recover
    public RefundResponse recover(RefundServiceDownError ex, RefundDto refundDto) {
        log.warn("Refund failed after retries for bookingId: " + refundDto.getBookingId());

        RefundResponse response = new RefundResponse();
        response.setStatus("Unsuccessful");
        response.setMessage("Refund failed after retries.");

        return response;
    }
}
