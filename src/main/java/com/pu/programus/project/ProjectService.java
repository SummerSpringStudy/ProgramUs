package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.exception.AuthorityException;
import com.pu.programus.keyword.Keyword;
import com.pu.programus.keyword.KeywordRepository;
import com.pu.programus.location.Location;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
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

        Optional<Location> optionalLocation = locationRepository.findByName(location);
        project.setLocation( optionalLocation
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
        // Todo: 잘못된 로직. dto를 추가하던가 해야함
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

        if(!ownerUid.equals(checkUid))
             throw new AuthorityException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    private void updateProjectData(ProjectRequestDTO projectRequestDTO, Project project) {

        clearProject(project);

        project.setTitle(projectRequestDTO.getTitle());
        project.setDescription(projectRequestDTO.getDescription());
        project.setStartTime(projectRequestDTO.getStartTime());
        project.setEndTime(projectRequestDTO.getEndTime());
        setLocation(projectRequestDTO, project);

        createMemberKeyword(projectRequestDTO, project);
        //Todo: 이미 인원있는 인원이 넘었는지 체크
        createProjectHeadCount(projectRequestDTO, project);
    }

    private void clearProject(Project project) {
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
        projectRepository.save(project);
        log.info("Project save {}", project);
    }

    /*
    public void apply(Long projectId, String positionName, String uid) {
        Project project = findProject(projectId);
        validateDuplicateApply(uid, project.getMemberProjects());
        applyUpdateHeadCount(project, positionName);
        addMemberToProject(uid, project, positionName);
    }
    */


    public void cancelApply(Long projectId, String positionName, String uid) {
        Project project = findProject(projectId);
        checkApplyStatus(uid, project.getMemberProjects());
        cancelUpdateHeadCount(project, positionName);
        removeMemberFromProject(uid, project);
    }

    private void checkApplyStatus(String uid, List<MemberProject> memberProjects) {
        if (!isAlreadyApply(uid, memberProjects))
            throw new IllegalArgumentException("지원하지 않은 프로젝트입니다.");
    }

    public ProjectMiniList getProjectMiniList(String title, String location, String position, Pageable pageable){

        List<Project> projects = projectRepository.findAllByLocationAndPosition(title, location, position, pageable);

        List<ProjectMiniResponseDTO> projectMiniResponseDTOS = projects.stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());

        return new ProjectMiniList(projectMiniResponseDTOS);
    }

    public ProjectResponseDTO getProjectById(Long projectId) {
        return ProjectResponseDTO.make(projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다.")));
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다."));
    }

    private void addMemberToProject(String uid, Project project, String positionName) {
        Member member = findMember(uid);
        Position position = findPosition(positionName);
        connectMemberProject(project, member, position);
        projectRepository.save(project);
    }

    private Position findPosition(String positionName) {
        return positionRepository.findByName(positionName).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 분야입니다."));
    }

    private void removeMemberFromProject(String uid, Project project) {
        Member member = findMember(uid);
        removeMemberProject(project, member);
        removeProjectFromMember(project, member);
    }

    private Member findMember(String uid) {
        return memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uid 입니다."));
    }

    private void removeProjectFromMember(Project project, Member member) {
        member.getMemberProjects().removeIf(p -> p.getId().equals(project.getId()));
    }

    private void connectMemberProject(Project project, Member member, Position position) {
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        memberProject.setPosition(position);
        project.addMemberProject(memberProject);
        projectRepository.save(project);
    }

    private void removeMemberProject(Project project, Member member) {
        memberProjectRepository.deleteByMemberIdAndProjectId(member.getId(), project.getId());
    }

    private void applyUpdateHeadCount(Project project, String positionName) {
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), positionName);
        validateApplicant(recruitInfo);
        increaseNowHeadCount(recruitInfo);
        projectHeadCountRepository.save(recruitInfo);
    }

    private void cancelUpdateHeadCount(Project project, String positionName) {
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), positionName);
        decreaseNowHeadCount(recruitInfo);
        projectHeadCountRepository.save(recruitInfo);
    }

    private void increaseNowHeadCount(ProjectHeadCount recruitInfo) {
        recruitInfo.setNowHeadCount(recruitInfo.getNowHeadCount() + 1);
    }

    private void decreaseNowHeadCount(ProjectHeadCount recruitInfo) {
        recruitInfo.setNowHeadCount(recruitInfo.getNowHeadCount() - 1);
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
