package com.niek125.tokenservice.repository;

import com.niek125.tokenservice.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, String> {
}
