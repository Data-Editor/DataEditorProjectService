package com.niek125.projectservice.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectservice.models.Permission;
import com.niek125.projectservice.models.Project;
import com.niek125.projectservice.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/project")
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectRepo projectRepo;
    private final ObjectMapper objectMapper;
    private final JWTVerifier jwtVerifier;

    @Autowired
    public ProjectController(ProjectRepo projectRepo, ObjectMapper objectMapper, JWTVerifier jwtVerifier) {
        this.projectRepo = projectRepo;
        this.objectMapper = objectMapper;
        this.jwtVerifier = jwtVerifier;
    }

    private boolean hasPermission(String token, String projectid) throws JsonProcessingException {
        logger.info("verifying token");
        final DecodedJWT jwt = jwtVerifier.verify(token.replace("Bearer ", ""));
        final Permission[] perms = objectMapper.readValue(((jwt.getClaims()).get("pms")).asString(), Permission[].class);
        return Arrays.stream(perms).anyMatch(p -> p.getProjectid().equals(projectid));
    }

    @GetMapping("/read/project/{projectid}")
    public Project getProject(@RequestHeader("Authorization") String token, @PathVariable("projectid") String projectid) throws JsonProcessingException {
        if(!hasPermission(token, projectid)){
            logger.info("no permission");
            return new Project();
        }
        logger.info("getting project");
        final Project project = projectRepo.findById(projectid).orElse(new Project());
        logger.info("returning project");
        return project;
    }

    @GetMapping("/read/projects")
    public List<Project> getProjects(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        logger.info("verifying token");
        final DecodedJWT jwt = jwtVerifier.verify(token.replace("Bearer ", ""));
        final Permission[] perms = objectMapper.readValue(jwt.getClaims().get("pms").asString(), Permission[].class);
        logger.info("getting projects");
        List<Project> projs = new ArrayList<>();
        for (Permission p :
                perms) {
            projs.add(projectRepo.findById(p.getProjectid()).orElse(new Project("-", "Not found")));
        }
        logger.info("returning projects");
        return projs;
    }
}
