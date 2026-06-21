package com.sample.learn.config;

import com.sample.learn.Contants.AppContant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic newTopic(){
        return TopicBuilder.name(AppContant.CAB_LOCATION_TOPIC).build();
    }
}
