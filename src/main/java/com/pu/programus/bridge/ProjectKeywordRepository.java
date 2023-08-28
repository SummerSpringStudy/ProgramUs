package com.pu.programus.bridge;

import com.pu.programus.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectKeywordRepository extends JpaRepository<ProjectKeyword, Long> {
    void deleteAllByProject(Project project);
}
