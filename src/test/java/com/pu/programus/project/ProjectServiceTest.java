package com.pu.programus.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProjectServiceTest {
    @Autowired
    ProjectService projectService;

    @Test
    @DisplayName("프로젝트 저장")
    void 프로젝트_저장() {
        Project project1 = new Project();

        System.out.println(project1.getMemberProjects());
    }
}
