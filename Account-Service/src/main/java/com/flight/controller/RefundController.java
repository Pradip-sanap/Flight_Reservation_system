package com.flight.controller;

import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.model.Refunds;
import com.flight.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/refund")
@RequiredArgsConstructor
@Slf4j
public class RefundController {

    private final RefundService refundService;

    @PostMapping()
    public ResponseEntity<RefundResponse> doRefund(@RequestBody RefundDto refundDto){
        log.info("Refund request received for userId={}, bookingId={}", refundDto.getUserId(), refundDto.getBookingId());
//        if(true){
//            throw new RuntimeException("Service UnAvailable");
//        }
        RefundResponse refunded = refundService.doRefund(refundDto);
        log.info("Refund request successful, with refunded amount={}", refunded.getRefundAmount());
        return new ResponseEntity<>(refunded, HttpStatus.CREATED);
    }
}
