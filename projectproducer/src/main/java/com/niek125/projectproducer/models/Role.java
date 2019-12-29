package com.niek125.projectproducer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
public class Role {
    private String roleid;
    private String userid;
    private String projectid;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
