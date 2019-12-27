package com.niek125.projectservice.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Permission {
    private String projectid;
    private String role;
}
