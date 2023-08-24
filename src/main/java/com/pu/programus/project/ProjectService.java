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
import com.pu.programus.project.DTO.HeadCountResponseDTO;
import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
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

    public void modify(Long id, Member member) {

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

        List<Project> projects;

        if(location.equals("전체") && position.equals("전체")) {
            projects = projectRepository.findAll();
        }
        else if (location.equals("전체")){
            projects = projectRepository.findAllByPosition(position, pageable);
        }
        else if (position.equals("전체")){
            projects = projectRepository.findAllByLocation(location, pageable);
        }
        else {
            projects = projectRepository.findAllByLocationAndPosition(location, position, pageable);
        }

        return projects.stream()
                .map(ProjectMiniResponseDTO::make)
                .collect(Collectors.toList());
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
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);

        // 추가
        project.addMemberProject(memberProject);

        // save
        memberProjectRepository.save(memberProject);
        projectRepository.save(project);
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
