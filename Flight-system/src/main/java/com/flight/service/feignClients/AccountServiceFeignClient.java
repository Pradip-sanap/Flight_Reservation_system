package com.flight.service.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Optional;

@FeignClient(name = "Account-Service", url = "http://localhost:8090")
public interface AccountServiceFeignClient {

    @PutMapping("/account/withdraw")
    Optional<Double> withdraw(@RequestParam Integer userId, @RequestParam Double amount);

    @PutMapping("/account/deposit")
    Optional<Double> deposit(@RequestParam Integer userId, @RequestParam Double amount);
}
