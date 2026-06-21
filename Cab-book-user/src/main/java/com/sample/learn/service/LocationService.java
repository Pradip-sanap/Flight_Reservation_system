package com.sample.learn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationService {

    @KafkaListener(topics = "cablocation", groupId = "user-group")
    public void cabLocation(String locationDataFromKafka){

        log.info(locationDataFromKafka);

    }
}
