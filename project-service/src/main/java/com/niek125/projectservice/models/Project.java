package com.niek125.projectservice.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity(name = "project")
public class Project {
    @Id
    private String projectid;
    private String projectname;
}
