package com.niek125.projectproducer.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.models.Action;
import com.niek125.projectproducer.models.KafkaHeader;
import com.niek125.projectproducer.models.KafkaMessage;
import com.niek125.projectproducer.models.Project;
import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ProjectHandler {
    private final DateTimeFormatter formatter;
    private final ObjectMapper objectMapper;

    public boolean validate(Project project) {
        if (
                !((Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(project.getProjectid()).matches()) &&
                        (project.getProjectname().length() > 0) &&
                        (project.getProjectname().length() < 257))) {
            return false;
        }
        return true;
    }

    public KafkaMessage construct(Project project) throws JsonProcessingException {
        return new KafkaMessage(new KafkaHeader(Action.CREATE, "project"),objectMapper.writeValueAsString(project));
    }
}
