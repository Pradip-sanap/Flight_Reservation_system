package com.sample.learn.controller;

import com.sample.learn.service.CabLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/location")
@RequiredArgsConstructor
@Slf4j
public class CabLocationController {

    private final CabLocationService cabLocationService;


    @PutMapping
    public ResponseEntity<Map<String, String>> updateLocation() throws InterruptedException {
        int a=1;
        while(a > 0){
            cabLocationService.updateLocationInKafka(Math.random() + " " + Math.random());
            Thread.sleep(1000);
            a--;
        }

        return  new ResponseEntity<>(Map.of("message", "LocationUpdated"), HttpStatus.OK);
    }

}
