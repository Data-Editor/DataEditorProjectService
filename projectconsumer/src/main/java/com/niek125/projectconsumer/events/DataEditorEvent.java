package com.niek125.projectconsumer.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataEditorEvent {
    private String creator;

    public DataEditorEvent() {
        this.creator = "project-consumer";
    }
}
