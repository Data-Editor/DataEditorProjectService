package com.niek125.projectproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProjectProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectProducerApplication.class, args);
    }

}
