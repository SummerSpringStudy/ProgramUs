package com.pu.programus.projectApply;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectHeadCount;
import com.pu.programus.project.ProjectRepository;
import com.pu.programus.projectApply.DTO.ProjectApplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    public void apply(ProjectApplyDTO projectApplyDTO, String uid ) {
        Project project = findProject(projectApplyDTO.getProjectId());
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), projectApplyDTO.getPosition().getName());
        validateApplicant(recruitInfo);    //지원하려는 포지션에 자리가 남았는 지
        validateDuplicateApply(uid, project.getMemberProjects());  //내가 이미 지원한 플젝은 아닌지

        setProjectApply(projectApplyDTO, project, member);
    }

    private void setProjectApply(ProjectApplyDTO projectApplyDTO, Project project, Member member) {
        ProjectApply projectApply = new ProjectApply();
        projectApply.setMember(member);
        projectApply.setIntro(projectApplyDTO.getIntro());
        projectApply.setProject(project);
        projectApply.setPosition(projectApplyDTO.getPosition());
        project.addProjectApply(projectApply);
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


    public void getMyApplyProjects(String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
        List<ProjectApply> myAppliedProjects = member.getAppliedProjects();
        //작업중

    }

    public void getMyConfirmProjects() {

    }

    public void getMyRejectProjects() {

    }

    public void getProjectApplicant() {

    }



}
