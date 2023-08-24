package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.keyword.Keyword;
import com.pu.programus.keyword.KeywordRepository;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.*;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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

        log.info("[create] projectRequestDTO: {}", projectRequestDTO);
        String location = projectRequestDTO.getLocation();

        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("uid 가 존재하지 않습니다."));
        List<String> keywords = projectRequestDTO.getKeywords();
        List<HeadCountResponseDTO> projectHeadCounts = projectRequestDTO.getProjectHeadCounts();

        Project project = Project.builder()
                .title(projectRequestDTO.getTitle())
                .description(projectRequestDTO.getDescription())
                .startTime(projectRequestDTO.getStartTime())
                .endTime(projectRequestDTO.getEndTime())
                .status(ProjectStatus.RECRUITING)
                .build();

        log.info("[create] project: {}", project);

        //Todo: exception 추가하기
        //Todo: 지역설정을 하지 않았을 경우
        //Todo: 없을경우
        project.setLocation(locationRepository.findByName(location)
                .orElseThrow(() -> new IllegalArgumentException("없는 지역입니다.")));

        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        memberProjectRepository.save(memberProject);

        member.addMemberProject(memberProject);
        memberRepository.save(member);

        log.info("[create] save project");

        for(String s : keywords){
            ProjectKeyword projectKeyword = new ProjectKeyword();

            Optional<Keyword> optionalKeyword = keywordRepository.findByValue(s);
            Keyword keyword = optionalKeyword.orElseGet(Keyword::new);

            if(keyword.getValue() == null || keyword.getValue().isEmpty()){
                keyword.setValue(s);
                keywordRepository.save(keyword);
                log.info("[create] Keyword save: {}", keyword);
            }

            projectKeyword.setProject(project);
            projectKeyword.setKeyword(keyword);
            projectKeywordRepository.save(projectKeyword);
        }

        for(HeadCountResponseDTO h : projectHeadCounts){
            ProjectHeadCount projectHeadCount = new ProjectHeadCount();
            projectHeadCount.setProject(project);
            //Todo: exception 추가하기
            // Todo: 없을경우
            projectHeadCount.setPosition(positionRepository.findByName(h.getPositionName())
                    .orElseThrow(() -> new IllegalArgumentException("없는 모집분야입니다.")));
            projectHeadCount.setNowHeadCount(h.getNowHeadCount());
            projectHeadCount.setMaxHeadCount(h.getMaxHeadCount());
            projectHeadCountRepository.save(projectHeadCount);
        }

        projectRepository.save(project);
    }

    public void delete(String uid, Long projectId){
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        // Todo: 멤버가 삭제 권한이 있는지 (그룹장인지) 확인 필요

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));

        log.info("[delete] project: {}", project);

        projectRepository.delete(project);
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

    public ProjectMiniList getProjectMiniList(String location, String position, Pageable pageable){

        List<Project> projects = projectRepository.findAllByLocationAndPosition(location, position, pageable);

        List<ProjectMiniResponseDTO> projectMiniResponseDTOS = projects.stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());

        ProjectMiniList projectMiniList = new ProjectMiniList(projectMiniResponseDTOS);
        return projectMiniList;
    }

    public ProjectResponseDTO getProjectById(Long projectId) {
        return ProjectResponseDTO.make(projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID입니다.")));
    }

}
