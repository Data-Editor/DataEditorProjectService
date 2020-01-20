package com.niek125.projectconsumer.kafka;

import com.niek125.projectconsumer.handlers.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProjectConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaProjectConsumer.class);
    private final EventHandler eventHandler;

    @Autowired
    public KafkaProjectConsumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "project", groupId = "project-consumer")
    public void consume(String message) {
        logger.info("received message: {}", message);
        eventHandler.processMessage(message);
        logger.info("successfully processed message");
    }
}
