package com.niek125.projectproducer.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.events.ProjectCreatedEvent;
import com.niek125.projectproducer.kafka.KafkaProducer;
import com.niek125.projectproducer.models.Project;
import com.niek125.projectproducer.validators.DataValidator;
import com.niek125.projectproducer.validators.ProjectValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final KafkaProducer producer;
    private final JWTVerifier jwtVerifier;
    private final ProjectValidator projectHandler;
    private final DataValidator dataHandler;
    @Value("${com.niek125.filedump}")
    private String filedir;

    @Autowired
    public ProjectController(ObjectMapper objectMapper, KafkaProducer producer, JWTVerifier jwtVerifier,
                             ProjectValidator projectHandler, DataValidator dataHandler) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.jwtVerifier = jwtVerifier;
        this.projectHandler = projectHandler;
        this.dataHandler = dataHandler;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public void createProject(@RequestHeader("Authorization") String token, @RequestParam("project") String prj, @RequestParam("file") MultipartFile file) throws IOException {
        final Project project = objectMapper.readValue(prj,Project.class);
        logger.info("verifying token");
        final DecodedJWT jwt = jwtVerifier.verify(token.replace("Bearer ", ""));
        logger.info("checking project format");
        if (!projectHandler.validate(project)) {
            logger.info("invalid format");
            return;
        }
        logger.info("checking data format");
        if (!dataHandler.validate(new String(file.getBytes()))) {
            logger.info("invalid format");
            return;
        }
        logger.info("constructing ProjectCreatedEvent");
        final ProjectCreatedEvent event = new ProjectCreatedEvent(project, jwt.getClaim("uid").asString());
        logger.info("sending event");
        producer.dispatch("project", event);
        logger.info("writing file: {}", project.getProjectid());
        final File fpath = new File(filedir + project.getProjectid() + ".json");
        file.transferTo(fpath);
        logger.info("successfully written file");
    }
}
