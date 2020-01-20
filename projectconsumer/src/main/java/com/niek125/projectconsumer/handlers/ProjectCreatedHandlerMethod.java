package com.niek125.projectconsumer.handlers;

import com.niek125.projectconsumer.events.DataEditorEvent;
import com.niek125.projectconsumer.events.ProjectCreatedEvent;
import com.niek125.projectconsumer.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectCreatedHandlerMethod extends HandlerMethod<ProjectCreatedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ProjectCreatedHandlerMethod.class);
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectCreatedHandlerMethod(ProjectRepo projectRepo) {
        super(ProjectCreatedEvent.class);
        this.projectRepo = projectRepo;
    }

    @Override
    public void handle(DataEditorEvent event) {
        logger.info("saving project");
        projectRepo.save(((ProjectCreatedEvent) event).getProject());
    }
}
