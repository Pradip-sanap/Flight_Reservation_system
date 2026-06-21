package com.sample.learn.service;

import com.sample.learn.Contants.AppContant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CabLocationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public boolean updateLocationInKafka(String location){
        kafkaTemplate.send(AppContant.CAB_LOCATION_TOPIC, location);
        return true;
    }
}
