package com.pu.programus.project;

import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class ProjectServiceTest {
    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    ProjectHeadCountRepository projectHeadCountRepository;

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

    @Test
    @Rollback(value = false)
    public void findProjectsByRecruitingPosition() {
        Project project1 = new Project();
        Project project2 = new Project();

        // 포지션 생성
        Position java = new Position();
        java.setName("JAVA");
        Position c = new Position();
        c.setName("C");
        Position cpp = new Position();
        cpp.setName("CPP");

        // ProjectHeadCount 생성
        ProjectHeadCount projectHeadCount1 = new ProjectHeadCount();
        projectHeadCount1.setPosition(java);
        projectHeadCount1.setProject(project1);
        ProjectHeadCount projectHeadCount2 = new ProjectHeadCount();
        projectHeadCount2.setPosition(c);
        projectHeadCount2.setProject(project1);
        ProjectHeadCount projectHeadCount3 = new ProjectHeadCount();
        projectHeadCount3.setPosition(cpp);
        projectHeadCount3.setProject(project2);

   /*     List<ProjectHeadCount> projectHeadCountList1 = new ArrayList<>();
        List<ProjectHeadCount> projectHeadCountList2 = new ArrayList<>();

        // List<ProjectHeadCount> 생성
        projectHeadCountList1.add(projectHeadCount1);
        projectHeadCountList1.add(projectHeadCount2);
        projectHeadCountList2.add(projectHeadCount1);
        projectHeadCountList2.add(projectHeadCount3);*/

  /*      // 프로젝트의 ProjectHeadCount 저장
        project1.setProjectHeadCounts(projectHeadCountList1);
        project2.setProjectHeadCounts(projectHeadCountList2);
*/
        // 포지션 저장
        positionRepository.save(cpp);

        // 프로젝트 저장
        projectRepository.save(project1);
        projectRepository.save(project2);

        // 중간 테이블 저장
        projectHeadCountRepository.save(projectHeadCount1);
        projectHeadCountRepository.save(projectHeadCount2);
        projectHeadCountRepository.save(projectHeadCount3);

        Position cppPosition = positionRepository.findByName("CPP")
                .orElseThrow(() -> new IllegalArgumentException("There's no Position"));

        List<Project> result = projectService.findProjectsByRecruitingPosition(cppPosition);

        Assertions.assertThat(result).contains(project2);
    }


}
