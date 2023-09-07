package com.pu.programus.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectHeadCountRepository extends JpaRepository<ProjectHeadCount, Long> {
    void deleteAllByProject(Project project);
}
