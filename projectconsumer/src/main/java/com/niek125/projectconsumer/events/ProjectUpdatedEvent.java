package com.niek125.projectconsumer.events;

import com.niek125.projectconsumer.models.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectUpdatedEvent extends DataEditorEvent {
    private Project project;

    public ProjectUpdatedEvent(String origin, Project project){
        super(origin);
        this.project = project;
    }
}
