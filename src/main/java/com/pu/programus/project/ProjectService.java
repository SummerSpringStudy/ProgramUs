package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.keyword.KeywordRepository;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.HeadCountResponseDTO;
import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectHeadCountRepository projectHeadCountRepository;
    private final ProjectKeywordRepository projectKeywordRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;
    private final KeywordRepository keywordRepository;
    private final MemberRepository memberRepository;

    public void create(String uid, ProjectRequestDTO projectRequestDTO) {

        String location = projectRequestDTO.getLocation();
        Member member = memberRepository.findByUid(uid).orElseThrow();
        List<String> keywords = projectRequestDTO.getKeywords();
        List<HeadCountResponseDTO> projectHeadCounts = projectRequestDTO.getProjectHeadCounts();

        Project project = Project.builder()
                .title(projectRequestDTO.getTitle())
                .description(projectRequestDTO.getDescription())
                .startTime(projectRequestDTO.getStartTime())
                .endTime(projectRequestDTO.getEndTime())
                .status(ProjectStatus.RECRUITING)
                .build();

        project.setLocation(locationRepository.findByName(location).orElseThrow());

        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        memberProjectRepository.save(memberProject);

        for(String s : keywords){
            ProjectKeyword projectKeyword = new ProjectKeyword();
            projectKeyword.setProject(project);
            projectKeyword.setKeyword(keywordRepository.findByValue(s).orElseThrow());
            projectKeywordRepository.save(projectKeyword);
        }

        for(HeadCountResponseDTO h : projectHeadCounts){
            ProjectHeadCount projectHeadCount = new ProjectHeadCount();
            projectHeadCount.setProject(project);
            projectHeadCount.setPosition(positionRepository.findByName(h.getPositionName()).orElseThrow());
            projectHeadCount.setNowHeadCount(h.getNowHeadCount());
            projectHeadCount.setMaxHeadCount(h.getMaxHeadCount());
            projectHeadCountRepository.save(projectHeadCount);
        }

        projectRepository.save(project);
    }

    public void modify(Long id, Member member) {

    }

    public void show(Long id, Member member) {

    }

    public void saveProject(Project project) {
        for (ProjectKeyword projectKeyword : project.getProjectKeywords()) {
            projectKeywordRepository.save(projectKeyword);
        }

        for (MemberProject memberProject : project.getMemberProjects()) {
            memberProjectRepository.save(memberProject);
        }

        for (ProjectHeadCount projectHeadCount : project.getProjectHeadCounts()) {
            projectHeadCountRepository.save(projectHeadCount);
        }
        projectRepository.save(project);
    }

    public List<Project> findProjectsByRecruitingPosition(Position pos) {
        List<ProjectHeadCount> projectHeadCounts = positionRepository.findByName(pos.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find " + pos.getName()))
                .getProjectHeadCounts();
        return getProjectsFromProjectHeadCounts(projectHeadCounts);
    }

    public List<Project> getProjectsByTitle(String title) {
        return projectRepository.findByTitle(title);
    }

    private List<Project> getProjectsFromProjectHeadCounts(List<ProjectHeadCount> projectHeadCounts) {
        List<Project> result = new ArrayList<>();
        for (ProjectHeadCount projectHeadCount : projectHeadCounts) {
            result.add(projectHeadCount.getProject());
        }
        return result;
    }

    public List<ProjectMiniResponseDTO> getMiniProjects(String location, String position, Pageable pageable){
        return projectRepository.findAllByLocationAndPosition(location, position, Pageable.ofSize(10))
                .stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());
    }

    public ProjectResponseDTO getProjectById(Long projectId) {
        return ProjectResponseDTO.make(projectRepository.findById(projectId).orElseThrow());
    }

}
