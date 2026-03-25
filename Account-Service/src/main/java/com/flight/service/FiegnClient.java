package com.flight.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "Service-A", url = "http:localhost:8080")
public interface FiegnClient{


}
