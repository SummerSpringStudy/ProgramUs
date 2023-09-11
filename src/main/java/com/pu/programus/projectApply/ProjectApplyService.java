package com.pu.programus.projectApply;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.ProjectList;
import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectHeadCount;
import com.pu.programus.project.ProjectHeadCountRepository;
import com.pu.programus.project.ProjectRepository;
import com.pu.programus.projectApply.DTO.ProjectApplicantDTO;
import com.pu.programus.projectApply.DTO.ProjectApplicantList;
import com.pu.programus.projectApply.DTO.ProjectApplyDTO;
import com.pu.programus.projectApply.DTO.ProjectApplyList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectApplyService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final PositionRepository positionRepository;
    private final ProjectApplyRepository projectApplyRepository;
    private final ProjectHeadCountRepository projectHeadCountRepository;

    public void apply(ProjectApplyDTO projectApplyDTO, String uid ) {
        Project project = findProject(projectApplyDTO.getProjectId());
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
        Position position = new Position(projectApplyDTO.getPosition());
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), position.getName());
        validateApplicant(recruitInfo);    //지원하려는 포지션에 자리가 남았는 지
        validateDuplicateApply(uid, project.getMemberProjects());  //내가 이미 지원한 플젝은 아닌지

        setProjectApply(projectApplyDTO, project, member, position);
    }

    private void setProjectApply(ProjectApplyDTO projectApplyDTO, Project project, Member member, Position position) {
        ProjectApply projectApply = new ProjectApply();
        projectApply.setMember(member);
        projectApply.setIntro(projectApplyDTO.getIntro());
        projectApply.setProject(project);
        projectApply.setPosition(position);
        project.addProjectApply(projectApply);
        projectApplyRepository.save(projectApply);
    }
    public void confirm(ProjectApplyDTO projectApplyDTO, String uid) {
        Project project = findProject(projectApplyDTO.getProjectId());
        Position position = findPosition(projectApplyDTO.getPosition());
        applyUpdateHeadCount(project,position.getName());
        addMemberToProject(uid,project, position.getName());

    }
    public static ProjectApplyList getMyApplyProjects(Member member) {
        List<ProjectApplyDTO> ApplyProjects = new ArrayList<>();
        for (ProjectApply pa : member.getAppliedProjects()) {
            Project project = pa.getProject();
            log.info("[getMyApplyProjects]: {}",project);
            ProjectApplyDTO dto = ProjectApplyDTO.builder()
                    .intro(pa.getIntro())
                    .projectId(project.getId())
                    .position(pa.getPosition().getName())
                    .build();
            ApplyProjects.add(dto);
        }
        return new ProjectApplyList(ApplyProjects);
    }

    public static ProjectApplicantList getProjectApplicants(Project project) {
        List<ProjectApplicantDTO> ProjectApplicants = new ArrayList<>();
        for (ProjectApply pa : project.getProjectApplicants()) {
            Member member = pa.getMember();
            log.info("[getProjectApplicants]: {}", member);
            ProjectApplicantDTO dto = ProjectApplicantDTO.builder()
                    .member(member)
                    .position(pa.getPosition().getName())
                    .intro(pa.getIntro())
                    .build();
            ProjectApplicants.add(dto);
        }
        return new ProjectApplicantList(ProjectApplicants);
    }


    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 ID 입니다."));
    }
    private void validateDuplicateApply(String uid, List<MemberProject> memberProjects) {
        if (isAlreadyApply(uid, memberProjects))
            throw new IllegalArgumentException("이미 지원한 모집 분야입니다.");
    }

    private void validateApplicant(ProjectHeadCount recruitInfo) {
        if (isHeadCountFull(recruitInfo))
            throw new IllegalArgumentException("지원한 모집 분야의 인원이 가득 찼습니다.");
    }

    private boolean isAlreadyApply(String uid, List<MemberProject> memberProjects) {
        return memberProjects.stream().anyMatch(e -> e.getMember().getUid().equals(uid));
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

    private void addMemberToProject(String uid, Project project, String positionName) {
        Member member = findMember(uid);
        Position position = findPosition(positionName);
        connectMemberProject(project, member, position);
        projectRepository.save(project);
    }

    private Member findMember(String uid) {
        return memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uid 입니다."));
    }
    private Position findPosition(String positionName) {
        return positionRepository.findByName(positionName).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 분야입니다."));
    }
    private void connectMemberProject(Project project, Member member, Position position) {
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        memberProject.setPosition(position);
        project.addMemberProject(memberProject);
        projectRepository.save(project);
    }

    private void applyUpdateHeadCount(Project project, String positionName) {
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), positionName);
        validateApplicant(recruitInfo);
        increaseNowHeadCount(recruitInfo);
        projectHeadCountRepository.save(recruitInfo);
    }

    private void increaseNowHeadCount(ProjectHeadCount recruitInfo) {
        recruitInfo.setNowHeadCount(recruitInfo.getNowHeadCount() + 1);
    }




    public void getMyConfirmProjects() {
        //멤버서비스의 나의 프로젝트 가져오기와 동일

    }

    public void getMyRejectProjects() {

    }





}
