package com.flight.service;

import com.flight.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Account-Service", url = "http://localhost:8090")
public interface AccountServiceFeignClient {

    @PutMapping("/account/withdraw")
    Double withdraw(@RequestParam Integer userId, @RequestParam Double amount);

    @PutMapping("/account/deposit")
    Double deposit(@RequestParam Integer userId, @RequestParam Double amount);
}
