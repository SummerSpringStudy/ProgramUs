package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.exception.AuthorityException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        setProjectOwner(uid, project);

        //Todo: 지역설정을 하지 않았을 경우
        setLocation(projectRequestDTO, project);

        createMemberProject(project, uid);

        createMemberKeyword(projectRequestDTO, project);

        createProjectHeadCount(projectRequestDTO, project);

        saveProject(project);
    }

    private void setProjectOwner(String uid, Project project) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("uid 가 존재하지 않습니다."));
        project.setOwner(member);
    }

    private void setLocation(ProjectRequestDTO projectRequestDTO, Project project) {
        String location = projectRequestDTO.getLocation();
        if (location == null)
            location = Project.ALL_LOCATION;

        project.setLocation(locationRepository.findByName(location)
                .orElseThrow(() -> new IllegalArgumentException("없는 지역입니다.")));
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

    public void update(String uid, Long projectId, ProjectRequestDTO projectRequestDTO) throws AuthorityException{

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));

        checkOwner(project, uid);

        updateProjectData(projectRequestDTO, project);

        log.info("complete update");

//        saveProject(project);
    }

    private void checkOwner(Project project, String checkUid) throws AuthorityException {

        String ownerUid = project.getOwner().getUid();

        if(!ownerUid.equals(checkUid)){
             throw new AuthorityException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
         }
    }

    private void updateProjectData(ProjectRequestDTO projectRequestDTO, Project project) {

        clear(project);

        project.setTitle(projectRequestDTO.getTitle());
        project.setDescription(projectRequestDTO.getDescription());
        project.setStartTime(projectRequestDTO.getStartTime());
        project.setEndTime(projectRequestDTO.getEndTime());
        setLocation(projectRequestDTO, project);

        createMemberKeyword(projectRequestDTO, project);
        createProjectHeadCount(projectRequestDTO, project);
    }

    private void clear(Project project) {
        projectHeadCountRepository.deleteAllByProject(project);
        projectKeywordRepository.deleteAllByProject(project);
        project.clear();
    }


    public void delete(String uid, Long projectId) throws AuthorityException{
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));

        checkOwner(project, uid);

        projectRepository.delete(project);
    }

    public void saveProject(Project project) {
        for (ProjectKeyword projectKeyword : project.getProjectKeywords()) {
            log.info("ProjectKeyword save {}", projectKeyword);
            projectKeywordRepository.save(projectKeyword);
        }

        // apply 시 에러 발생
        for (MemberProject memberProject : project.getMemberProjects()) {
            log.info("MemberProject save {}", memberProject);
            memberProjectRepository.save(memberProject);
        }

        for (ProjectHeadCount projectHeadCount : project.getProjectHeadCounts()) {
            log.info("ProjectHeadCount save {}", projectHeadCount);
            projectHeadCountRepository.save(projectHeadCount);
        }

        projectRepository.save(project);
        log.info("Project save {}", project);
    }

    public void apply(Long projectId, String positionName, String uid) {
        Project project = findProject(projectId);
        validateDuplicateApply(uid, project.getMemberProjects());
        updateHeadCountByPositionName(project, positionName);
        addMemberToProject(uid, project);
    }

    // Todo: 제목 조회 api만들기
    public ProjectMiniList getProjectsContainsTitle(String title) {
        List<Project> projects = projectRepository.findByTitleContains(title);
        List<ProjectMiniResponseDTO> projectMiniResponseDTOS = projects.stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());
        return new ProjectMiniList(projectMiniResponseDTOS);
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

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다."));
    }

    private void addMemberToProject(String uid, Project project) {
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
        projectRepository.save(project);
    }

    private void updateHeadCountByPositionName(Project project, String positionName) {
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), positionName);
        validateApplicant(recruitInfo);
        increaseNowHeadCount(recruitInfo);
        projectHeadCountRepository.save(recruitInfo);
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

    private void validateApplicant(ProjectHeadCount recruitInfo) {
        if (isHeadCountFull(recruitInfo))
            throw new IllegalArgumentException("지원한 모집 분야의 인원이 가득 찼습니다.");
    }

    private boolean isHeadCountFull(ProjectHeadCount recruitInfo) {
        return recruitInfo.getMaxHeadCount() == recruitInfo.getNowHeadCount();
    }

    private ProjectHeadCount getRecruitInfo(List<ProjectHeadCount> projectHeadCounts, String positionName) {
        return projectHeadCounts.stream()
                .filter(e -> e.getPosition().getName().equals(positionName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 분야입니다."));
    }
}
