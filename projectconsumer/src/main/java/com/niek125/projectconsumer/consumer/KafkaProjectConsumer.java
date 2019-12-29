package com.niek125.projectconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.projectconsumer.models.Action;
import com.niek125.projectconsumer.models.KafkaHeader;
import com.niek125.projectconsumer.models.KafkaMessage;
import com.niek125.projectconsumer.models.Project;
import com.niek125.projectconsumer.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProjectConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaProjectConsumer.class);
    private final ObjectMapper objectMapper;
    private final ProjectRepo projectRepo;

    @Autowired
    public KafkaProjectConsumer(ObjectMapper objectMapper, ProjectRepo projectRepo) {
        this.objectMapper = objectMapper;
        this.projectRepo = projectRepo;
    }

    @KafkaListener(topics = "project", groupId = "project-consumer")
    public void consume(String message) throws JsonProcessingException {
        logger.info("received message: " + message);
        logger.info("parsing project");
        final String[] pay = message.split("\n");
        final DocumentContext doc = JsonPath.parse(pay[0]);
        final KafkaMessage kafkaMessage = new KafkaMessage(new KafkaHeader(Action.valueOf(doc.read("$.action")), doc.read("$.payload")), pay[1]);
        final Project project = objectMapper.readValue(kafkaMessage.getPayload(), Project.class);
        switch (kafkaMessage.getKafkaHeader().getAction()) {
            case CREATE:
            case UPDATE:
                logger.info("saving project");
                projectRepo.save(project);
                break;
            case DELETE:
                logger.info("deleting project");
                projectRepo.deleteById(project.getProjectid());
                break;
        }
        logger.info("successfully processed message");
    }
}
