package com.pmu.courses_manager.infrastructure.adapter.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "course-created", groupId = "my-group")
    public void courseCreationListner(String message) {
        System.out.println("Course Created: " + message);
    }
    @KafkaListener(topics = "participant-added", groupId = "my-group")
    public void participantAddListner(String message) {
        System.out.println("Participant Added: " + message);
    }
}
