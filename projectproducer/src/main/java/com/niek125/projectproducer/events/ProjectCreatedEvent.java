package com.niek125.projectproducer.events;

import com.niek125.projectproducer.models.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectCreatedEvent extends DataEditorEvent {
    private Project project;
    private String creatorid;

    public ProjectCreatedEvent(String origin, Project project, String creatorid) {
        super(origin);
        this.project = project;
        this.creatorid = creatorid;
    }
}
