package com.niek125.projectconsumer.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HandlerConfig {
    @Bean
    @Autowired
    public List<HandlerMethod> handlerMethods(ProjectCreatedHandlerMethod projectCreatedHandlerMethod,
                                              ProjectUpdatedHandlerMethod projectUpdatedHandlerMethod,
                                              ProjectDeletedHandlerMethod projectDeletedHandlerMethod) {
        final List<HandlerMethod> methods = new ArrayList<>();
        methods.add(projectCreatedHandlerMethod);
        methods.add(projectUpdatedHandlerMethod);
        methods.add(projectDeletedHandlerMethod);
        return methods;
    }
}
