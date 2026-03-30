package com.flight.service.feignClients;

import com.flight.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.util.Optional;

@FeignClient(value = "payment-service", url = "http://localhost:8090/account/payment")
public interface PaymentServiceFeignClient {

    @GetMapping("/{paymentId}")
    Optional<PaymentDto> getPaymentById(@PathVariable("paymentId") Integer payId);

    @PostMapping()
    Optional<PaymentDto> createNewPayment(@RequestBody PaymentDto payment);
}
