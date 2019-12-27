package com.niek125.projectproducer.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Role {
    private String roleid;
    private String userid;
    private String projectid;
    private RoleType role;
}
