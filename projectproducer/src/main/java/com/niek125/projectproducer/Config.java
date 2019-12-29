package com.niek125.projectproducer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.handlers.DataHandler;
import com.niek125.projectproducer.handlers.ProjectHandler;
import com.niek125.projectproducer.handlers.RoleHandler;
import com.niek125.projectproducer.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.format.DateTimeFormatter;

import static com.niek125.projectproducer.utils.PemUtils.readPublicKeyFromFile;

@Configuration
public class Config {
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RoleHandler roleHandler(){
        return new RoleHandler(new ObjectMapper());
    }

    @Bean
    public DataHandler dataHandler(){
        return new DataHandler();
    }

    @Bean
    public ProjectHandler projectHandler(){
        return  new ProjectHandler(DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm"), new ObjectMapper());
    }

    @Bean
    @Autowired
    public KafkaProducer kafkaDispatcher(KafkaTemplate<String, String> template) {
        return new KafkaProducer(template, new ObjectMapper());
    }

    @Bean
    public JWTVerifier jwtVerifier() throws IOException {
        final Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile("project-service/src/main/resources/PublicKey.pem", "RSA"), null);
        return JWT.require(algorithm).withIssuer("data-editor-token-service").build();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartCommonResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
