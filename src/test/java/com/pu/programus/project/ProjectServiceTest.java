package com.pu.programus.project;

import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.keyword.Keyword;
import com.pu.programus.keyword.KeywordRepository;
import com.pu.programus.location.Location;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.HeadCountResponseDTO;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void 프로젝트_생성(){

        // 멤버 생성 및 저장
        Member member = new Member();
        member.setUserName("name");
        member.setUid("test");
        member.setPassword("pw");
        memberRepository.save(member);

        // 지역 생성 및 저장
        Location location = new Location();
        location.setName("location");
        locationRepository.save(location);

        Date sDate = new Date();
        Date eDate = new Date();

        // 키워드 생성 및 저장
        List<String> keywords = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            keywords.add("key"+i);

            Keyword keyword = new Keyword();
            keyword.setValue("key"+i);
            keywordRepository.save(keyword);
        }

        //HeadCountResponseDTO List 생성, Position 생성 및 저장
        List<HeadCountResponseDTO> headCountResponseDTOS = new ArrayList<>();

        for (int i = 1; i <=3 ; i++) {
            HeadCountResponseDTO h = new HeadCountResponseDTO("p"+i, i, i);
            headCountResponseDTOS.add(h);

            Position position = new Position();
            position.setName("p"+i);
            positionRepository.save(position);
        }

        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO(
                "test", "title", "location" ,"description",
                sDate, eDate, keywords, headCountResponseDTOS);

        // 프로젝트 생성
        projectService.create("test", projectRequestDTO);

        em.flush();
        em.clear();

        List<Project> all = projectRepository.findAll();
        Project project = all.get(0);

        assertThat(project.getTitle()).isEqualTo("title");
        assertThat(project.getLocation().getName()).isEqualTo("location");
        assertThat(project.getDescription()).isEqualTo("description");
        assertThat(project.getProjectKeywords().size()).isEqualTo(3);
        assertThat(project.getProjectHeadCounts().size()).isEqualTo(3);
        assertThat(project.getMemberProjects().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로젝트 요약 정보 가져오기")
    void 프로젝트_목록_가져오기() {

        Project project1 = new Project();
        Project project2 = new Project();
        Project project3 = new Project();

        Location location = new Location();
        location.setName("서울");

        Position position = new Position();
        position.setName("전체");

        project1.setLocation(location);
        project2.setLocation(location);

        // 주인인 ProjectHeadCount에 Project를 매핑하여 저장해야 함
        ProjectHeadCount projectHeadCount1 = new ProjectHeadCount();
        projectHeadCount1.setPosition(position);
        projectHeadCount1.setProject(project1);
        projectHeadCountRepository.save(projectHeadCount1);

        ProjectHeadCount projectHeadCount2 = new ProjectHeadCount();
        projectHeadCount2.setPosition(position);
        projectHeadCount2.setProject(project2);
        projectHeadCountRepository.save(projectHeadCount2);

        projectService.saveProject(project1);
        projectService.saveProject(project2);
        projectService.saveProject(project3);

        em.flush();
        em.clear();

        List<Project> results = projectRepository.findAllByLocationAndPosition("서울", "전체", Pageable.unpaged());

        assertThat(results.size()).isEqualTo(2);

        System.out.println("results = " + results.size());
        System.out.println("results.get(0) = " + results.get(0).getLocation().getName() +
                            " results.get(0) = " + results.get(0).getProjectHeadCounts().get(0).getPosition().getName() +
                            " results.get(0) = " + results.get(0).getId());
        System.out.println("results.get(1) = " + results.get(1).getLocation().getName() +
                            " results.get(1) = " + results.get(1).getProjectHeadCounts().get(0).getPosition().getName() +
                            " results.get(1) = " + results.get(1).getId());
    }

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
        ProjectHeadCount projectHeadCount1 = ProjectHeadCount.builder()
                .position(java)
                .project(project1)
                .build();
        java.addProjectHeadCount(projectHeadCount1);
        project1.addProjectHeadCount(projectHeadCount1);

        ProjectHeadCount projectHeadCount2 = ProjectHeadCount.builder()
                .position(c)
                .project(project1)
                .build();
        c.addProjectHeadCount(projectHeadCount2);
        project1.addProjectHeadCount(projectHeadCount2);

        ProjectHeadCount projectHeadCount3 = ProjectHeadCount.builder()
                .position(java)
                .project(project2)
                .build();
        cpp.addProjectHeadCount(projectHeadCount3);
        project2.addProjectHeadCount(projectHeadCount3);

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
        assertThat(result).contains(project2);
    }


}
