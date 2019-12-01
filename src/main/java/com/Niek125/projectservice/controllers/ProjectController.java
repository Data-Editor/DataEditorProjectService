package com.Niek125.projectservice.controllers;

import com.Niek125.dataadapterlibrary.DataBase.DataBase;
import com.Niek125.dataadapterlibrary.DataBaseBuilder;
import com.Niek125.dataadapterlibrary.MySQLDataBaseBuilder;
import com.Niek125.projectservice.models.Project;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private DataBase db;

    public ProjectController() {
        DataBaseBuilder builder = new MySQLDataBaseBuilder("jdbc:mysql://127.0.0.1/dataeditorprojectservice", "root", "");
        db = builder.getDataBase();
    }

    @RequestMapping("/create/{project}")
    public void createProject(@PathVariable String project) {

    }

    @RequestMapping("/read/{projectid}")
    public String getProject(@PathVariable String projectid) {
        return null;
    }

    @RequestMapping("/read/projects")
    public String getProjects() {
        db.queryBuilder().storedProcedure("userprojects");
        List<String> search = new ArrayList<>();
        search.add("240529d6-0ebb-11ea-ba3c-1062e58ec6e2");
        search.add("38ce36f3-0ebb-11ea-ba3c-1062e58ec6e2");
        db.queryBuilder().addParameter(search);
        List<Project> projs = db.executeReturnQuery(Project.class);
        String json = "[";
        for (int i = 0; i < projs.size(); i++) {
            if (i > 0) {
                json += ",";
            }
            json += projs.get(i).toJSON("value", "text");
        }
        return json + "]";
    }

    @RequestMapping("/update/{project}")
    public void updateProject() {

    }

    @RequestMapping("/delete/{projectid}")
    public void deleteProject() {

    }
}
