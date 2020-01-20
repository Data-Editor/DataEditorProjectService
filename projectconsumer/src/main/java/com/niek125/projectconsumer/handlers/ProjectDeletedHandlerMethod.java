package com.niek125.projectconsumer.handlers;

import com.niek125.projectconsumer.events.DataEditorEvent;
import com.niek125.projectconsumer.events.ProjectDeletedEvent;
import com.niek125.projectconsumer.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProjectDeletedHandlerMethod extends HandlerMethod<ProjectDeletedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ProjectDeletedHandlerMethod.class);
    private final ProjectRepo projectRepo;

    public ProjectDeletedHandlerMethod(ProjectRepo projectRepo) {
        super(ProjectDeletedEvent.class);
        this.projectRepo = projectRepo;
    }

    @Override
    public void handle(DataEditorEvent event) {
        logger.info("deleting project");
        projectRepo.deleteById(((ProjectDeletedEvent) event).getProjectid());
    }
}
