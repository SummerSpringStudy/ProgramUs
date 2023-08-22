package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.member.Member;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberProjectRepository memberProjectRepository;
    @Autowired
    private ProjectHeadCountRepository projectHeadCountRepository;
    @Autowired
    private ProjectKeywordRepository projectKeywordRepository;
    @Autowired
    private PositionRepository positionRepository;

    public void create(Member member) {

    }

    public void modify(Long id, Member member) {

    }

    public void show(Long id, Member member) {

    }

    //Todo: 테스트코드 만들어보기
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

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
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
}
