package com.niek125.projectproducer.handlers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.models.*;

import java.util.UUID;

public class RoleHandler {
    private final ObjectMapper objectMapper;

    public RoleHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public KafkaMessage construct(Project project, DecodedJWT jwt) throws JsonProcessingException {
        final Role role = new Role(UUID.randomUUID().toString(), jwt.getClaim("uid").asString(), project.getProjectid(), RoleType.OWNER);
        return new KafkaMessage(new KafkaHeader(Action.CREATE, "role"), objectMapper.writeValueAsString(role));
    }
}
