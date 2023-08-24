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
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

        Project project = buildProjectByPrimitiveValue(projectRequestDTO);

        //Todo: 지역설정을 하지 않았을 경우
        setLocation(projectRequestDTO, project);

        createMemberProject(project, uid);

        createMemberKeyword(projectRequestDTO, project);

        createProjectHeadCount(projectRequestDTO, project);

        saveProject(project);
    }

    private void setLocation(ProjectRequestDTO projectRequestDTO, Project project) {
        String location = projectRequestDTO.getLocation();
        if (location != null) {
            project.setLocation(locationRepository.findByName(location)
                    .orElseThrow(() -> new IllegalArgumentException("없는 지역입니다.")));
        }
    }

    private void createProjectHeadCount(ProjectRequestDTO projectRequestDTO, Project project) {
        List<HeadCountResponseDTO> projectHeadCounts = projectRequestDTO.getProjectHeadCounts();
        for(HeadCountResponseDTO h : projectHeadCounts){
            ProjectHeadCount projectHeadCount = new ProjectHeadCount();
            projectHeadCount.setProject(project);
            project.addProjectHeadCount(projectHeadCount);
            //Todo: exception 추가하기
            // Todo: 없을경우
            projectHeadCount.setPosition(positionRepository.findByName(h.getPositionName())
                    .orElseThrow(() -> new IllegalArgumentException("없는 모집분야입니다.")));
            projectHeadCount.setNowHeadCount(h.getNowHeadCount());
            projectHeadCount.setMaxHeadCount(h.getMaxHeadCount());
        }
    }

    private void createMemberKeyword(ProjectRequestDTO projectRequestDTO, Project project) {
        List<String> keywords = projectRequestDTO.getKeywords();
        for(String s : keywords){
            ProjectKeyword projectKeyword = new ProjectKeyword();

            //Todo: 로직 분리의 필요성
            Optional<Keyword> optionalKeyword = keywordRepository.findByValue(s);
            Keyword keyword = optionalKeyword.orElseGet(Keyword::new);

            if(keyword.getValue() == null || keyword.getValue().isEmpty()){
                keyword.setValue(s);
                keywordRepository.save(keyword);
                log.info("[create] Keyword save: {}", keyword);
            }

            projectKeyword.setProject(project);
            projectKeyword.setKeyword(keyword);
            project.addProjectKeyword(projectKeyword);
        }
    }

    private void createMemberProject(Project project, String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("uid 가 존재하지 않습니다."));

        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        memberProject.setPosition(member.getPosition());
        project.addMemberProject(memberProject);

        member.addMemberProject(memberProject);
    }

    private static Project buildProjectByPrimitiveValue(ProjectRequestDTO projectRequestDTO) {
        return Project.builder()
                .title(projectRequestDTO.getTitle())
                .description(projectRequestDTO.getDescription())
                .startTime(projectRequestDTO.getStartTime())
                .endTime(projectRequestDTO.getEndTime())
                .status(ProjectStatus.RECRUITING)
                .build();
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
    
    // Todo: editProject필요

    // Comment: 필요없어짐
    /*
    public List<Project> findProjectsByRecruitingPosition(Position pos) {
        List<ProjectHeadCount> projectHeadCounts = positionRepository.findByName(pos.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find " + pos.getName()))
                .getProjectHeadCounts();
        return getProjectsFromProjectHeadCounts(projectHeadCounts);
    }
     */

    // Todo: 제목 조회 api만들기
    public ProjectMiniList getProjectsContainsTitle(String title) {
        List<Project> projects = projectRepository.findByTitleContains(title);
        List<ProjectMiniResponseDTO> projectMiniResponseDTOS = projects.stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());
        return new ProjectMiniList(projectMiniResponseDTOS);
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다.")));
    }

    public void apply(Long projectId, String positionName, String uid) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다."));
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), positionName);

        validateDuplicateApply(uid, project.getMemberProjects());
        validateApply(recruitInfo);
        increaseNowHeadCount(recruitInfo);

        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uid 입니다."));
        connectMemberProject(project, member);
        saveProject(project);
    }

    private void connectMemberProject(Project project, Member member) {
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        project.addMemberProject(memberProject);
    }

    private void increaseNowHeadCount(ProjectHeadCount recruitInfo) {
        recruitInfo.setNowHeadCount(recruitInfo.getNowHeadCount() + 1);
    }

    private void validateDuplicateApply(String uid, List<MemberProject> memberProjects) {
        if (isAlreadyApply(uid, memberProjects))
            throw new IllegalArgumentException("이미 지원한 모집 분야입니다.");
    }

    private boolean isAlreadyApply(String uid, List<MemberProject> memberProjects) {
        return memberProjects.stream().anyMatch(e -> e.getMember().getUid().equals(uid));
    }

    private void validateApply(ProjectHeadCount recruitInfo) {
        if (isHeadCountFull(recruitInfo))
            throw new IllegalArgumentException("지원한 모집 분야의 인원이 가득 찼습니다.");
    }

    private static boolean isHeadCountFull(ProjectHeadCount recruitInfo) {
        return recruitInfo.getMaxHeadCount() == recruitInfo.getNowHeadCount();
    }

    private ProjectHeadCount getRecruitInfo(List<ProjectHeadCount> projectHeadCounts, String positionName) {
        return projectHeadCounts.stream()
                .filter(e -> e.getPosition().getName().equals(positionName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 분야입니다."));
    }
}
