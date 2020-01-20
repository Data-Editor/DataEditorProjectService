package com.niek125.projectconsumer.handlers;

import com.niek125.projectconsumer.events.DataEditorEvent;
import com.niek125.projectconsumer.events.ProjectUpdatedEvent;
import com.niek125.projectconsumer.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdatedHandlerMethod extends HandlerMethod<ProjectUpdatedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ProjectUpdatedHandlerMethod.class);
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectUpdatedHandlerMethod(ProjectRepo projectRepo) {
        super(ProjectUpdatedEvent.class);
        this.projectRepo = projectRepo;
    }

    @Override
    public void handle(DataEditorEvent event) {
        logger.info("updating project");
        projectRepo.save(((ProjectUpdatedEvent) event).getProject());
    }
}
