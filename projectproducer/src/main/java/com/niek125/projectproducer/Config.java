package com.niek125.projectproducer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.projectproducer.kafka.KafkaProducer;
import com.niek125.projectproducer.validators.DataValidator;
import com.niek125.projectproducer.validators.ProjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

import static com.niek125.projectproducer.utils.PemUtils.readPublicKeyFromFile;

@Configuration
public class Config {
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public DataValidator dataHandler(){
        return new DataValidator();
    }

    @Bean
    public ProjectValidator projectHandler(){
        return  new ProjectValidator();
    }

    @Bean
    @Autowired
    public KafkaProducer kafkaDispatcher(KafkaTemplate<String, String> template) {
        return new KafkaProducer(template, new ObjectMapper());
    }

    @Value("${com.niek125.publickey}")
    private String publickey;

    @Value("${com.niek125.allowed-token-signer}")
    private String tokenSigner;

    @Bean
    public JWTVerifier jwtVerifier() throws IOException {
        final Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile(publickey, "RSA"), null);
        return JWT.require(algorithm).withIssuer(tokenSigner).build();
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
