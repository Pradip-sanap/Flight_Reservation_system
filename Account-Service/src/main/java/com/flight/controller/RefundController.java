package com.flight.controller;

import com.flight.dto.RefundDto;
import com.flight.dto.response.RefundResponse;
import com.flight.model.Refunds;
import com.flight.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/refund")
@RequiredArgsConstructor
public class RefundController {
    private final RefundService refundService;

    @PostMapping()
    public ResponseEntity<RefundResponse> doRefund(@RequestBody RefundDto refundDto){
        RefundResponse refunded = refundService.saveRefundDetails(refundDto);
        return new ResponseEntity<>(refunded, HttpStatus.CREATED);
    }
}
