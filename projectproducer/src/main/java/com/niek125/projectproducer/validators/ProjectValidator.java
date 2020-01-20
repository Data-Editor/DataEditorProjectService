package com.niek125.projectproducer.validators;

import com.niek125.projectproducer.models.Project;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@AllArgsConstructor
public class ProjectValidator implements Validator<Project> {
    private final Logger logger = LoggerFactory.getLogger(ProjectValidator.class);

    public boolean validate(Project project) {
        if (
                !((Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(project.getProjectid()).matches()) &&
                        (project.getProjectname().length() > 0) &&
                        (project.getProjectname().length() < 257))) {
            logger.info("project is invalid");
            return false;
        }
        return true;
    }
}
