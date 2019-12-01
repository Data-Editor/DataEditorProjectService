package com.Niek125.projectservice.models;

public class Project {
    private String projectid;
    private String projectname;

    public Project(){

    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String toJSON(String aliasPID, String aliasPNM){
        return "{\"" + aliasPID + "\":\"" + projectid + "\",\"" + aliasPNM + "\":\"" + projectname + "\"}";
    }
}
