package com.pu.programus.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProjectServiceTest {
    @Autowired
    ProjectService projectService;

    @Test
    @DisplayName("프로젝트 저장")
    void 프로젝트_저장() {
        Project project = new Project();
        project.setTitle("정환이의 프로젝트");
        project.setDescription("프로젝트 설명");

        //Todo: position까지 저장되는지 테스트 추가하기
        ProjectHeadCount projectHeadCount = new ProjectHeadCount();
        projectHeadCount.setProject(project);
        projectHeadCount.setMaxHeadCount(3);
        projectHeadCount.setNowHeadCount(1);
        project.getProjectHeadCounts().add(projectHeadCount);

        projectService.saveProject(project);

        List<Project> findProjects = projectService.getProjectsByTitle("정환이의 프로젝트");
        assertThat(findProjects.size()).isEqualTo(1);
        System.out.println("findProjects = " + findProjects);
        Project findProject = findProjects.get(0);// 저장에 문제가 있음
        assertThat(findProject.getProjectHeadCounts().get(0).getMaxHeadCount()).isEqualTo(3);
    }
}
