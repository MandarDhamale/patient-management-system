package com.pm.patientservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;

public class kafkaProducer {
    // responsible for sending events to a given kafka topic
    // kafka template
    // we are going to be sending a kafka event of key value pair (String, byte[])
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
}
