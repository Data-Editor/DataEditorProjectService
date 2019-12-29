package com.niek125.projectproducer.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.handlers.DataHandler;
import com.niek125.projectproducer.handlers.ProjectHandler;
import com.niek125.projectproducer.handlers.RoleHandler;
import com.niek125.projectproducer.kafka.KafkaProducer;
import com.niek125.projectproducer.models.KafkaMessage;
import com.niek125.projectproducer.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final KafkaProducer producer;
    private final JWTVerifier jwtVerifier;
    private final ProjectHandler projectHandler;
    private final DataHandler dataHandler;
    private final RoleHandler roleHandler;
    private final ObjectMapper objectMapper;
    @Value("${com.niek125.filedump}")
    private String filedir;

    @Autowired
    public ProjectController(KafkaProducer producer, JWTVerifier jwtVerifier, ProjectHandler projectHandler, DataHandler dataHandler, RoleHandler roleHandler, ObjectMapper objectMapper) {
        this.producer = producer;
        this.jwtVerifier = jwtVerifier;
        this.projectHandler = projectHandler;
        this.dataHandler = dataHandler;
        this.roleHandler = roleHandler;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void createProject(@RequestHeader("Authorization") String token, @RequestParam("project") String prj, @RequestParam("file") MultipartFile file) throws IOException {
        final Project project = objectMapper.readValue(prj, Project.class);
        System.out.println(file.getName());
        logger.info("verifying token");
        final DecodedJWT jwt = jwtVerifier.verify(token.replace("Bearer ", ""));
        logger.info("checking project format");
        if (!projectHandler.validate(project)) {
            logger.info("invalid format");
            return;
        }
        logger.info("checking data format");
        if (!dataHandler.validate(new String(file.getBytes()))){
            logger.info("invalid format");
            return;
        }
        logger.info("constructing projectmessage");
        final KafkaMessage projectMessage = projectHandler.construct(project);
        logger.info("constructing rolemessage");
        final KafkaMessage roleMessage = roleHandler.construct(project, jwt);
        logger.info("sending projectmessage");
        producer.dispatch(projectMessage);
        logger.info("sending rolemessage");
        producer.dispatch(roleMessage);
        logger.info("writing file: " + project.getProjectid());
        final File fpath = new File(filedir + project.getProjectid() + ".json");
        file.transferTo(fpath);
        logger.info("successfully written file");
    }
}
