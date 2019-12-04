package com.niek125.tokenservice.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.tokenservice.models.Permission;
import com.niek125.tokenservice.models.Project;
import com.niek125.tokenservice.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.niek125.tokenservice.controllers.PemUtils.readPublicKeyFromFile;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectRepo projectRepo;
    private ObjectMapper objectMapper;

    public ProjectController() {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping("/create/{project}")
    public void createProject(@PathVariable String project) {

    }

    @RequestMapping("/read/project/{projectid}")
    public Project getProject(@RequestHeader("Authorization") String token, @PathVariable("projectid") String projectid) {
        try {
            Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile("src/main/resources/PublicKey.pem", "RSA"), null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("data-editor-token-service")
                    .build();
            DecodedJWT jwt = verifier.verify(token.replace("Bearer ", ""));
            Permission[] perms = objectMapper.readValue(((jwt.getClaims()).get("pms")).asString(), Permission[].class);
            if(Arrays.stream(perms).filter(p -> p.getProjectid().equals(projectid)).findFirst().isPresent()){
                return projectRepo.findById(projectid).orElse(new Project());
            }
        } catch (JWTVerificationException | IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/read/projects")
    public String getProjects(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile("src/main/resources/PublicKey.pem", "RSA"), null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("data-editor-token-service")
                    .build();
            jwt = verifier.verify(token.replace("Bearer ", ""));
        } catch (JWTVerificationException | IOException exception) {
            exception.printStackTrace();
            return null;
        }
        Permission[] perms = objectMapper.readValue(((jwt.getClaims()).get("pms")).asString(), Permission[].class);
        List<Project> projs = new ArrayList<>();
        for (Permission p :
                perms) {
            projs.add(projectRepo.getOne(p.getProjectid()));
        }
        String json = "[";
        for (int i = 0; i < projs.size(); i++) {
            if (i > 0) {
                json += ",";
            }
            json += projs.get(i).toJSON("value", "text");
        }
        return json + "]";
    }

    @RequestMapping("/update/{project}")
    public void updateProject() {

    }

    @RequestMapping("/delete/{projectid}")
    public void deleteProject() {

    }
}
