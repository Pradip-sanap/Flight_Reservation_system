package com.flight.service.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/user")
public interface UserServiceFeignClient {

    @GetMapping("/check/{userId}")
    Boolean validateUser(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token
    );

}
