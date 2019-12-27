package com.niek125.projectservice.repository;

import com.niek125.projectservice.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, String> {
}
