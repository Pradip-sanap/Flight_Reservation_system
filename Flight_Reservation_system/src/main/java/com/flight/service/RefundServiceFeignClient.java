package com.flight.service;

import com.flight.dto.RefundDto;
import com.flight.dto.RefundResponse;
import com.flight.repository.RefundRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "refund-service", url = "http://localhost:8090/account/refund")
public interface RefundServiceFeignClient {

    @PostMapping()
    RefundResponse doRefund(@RequestBody RefundDto refundDto);
}
