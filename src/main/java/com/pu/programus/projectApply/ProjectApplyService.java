package com.pu.programus.projectApply;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectHeadCount;
import com.pu.programus.project.ProjectRepository;
import com.pu.programus.projectApply.DTO.ProjectApplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectApplyService {
    private final ProjectRepository projectRepository;


    public void apply(ProjectApplyDTO projectApplyDTO, String uid ) {
        Project project = findProject(projectApplyDTO.getProjectId());
        ProjectHeadCount recruitInfo = getRecruitInfo(project.getProjectHeadCounts(), projectApplyDTO.getPositionDTO().getName());
        validateApplicant(recruitInfo);    //지원하려는 포지션에 자리가 남았는지
        validateDuplicateApply(projectApplyDTO.getUid(), project.getMemberProjects());  //내가 이미 지원한 플젝은 아닌지

        //+프로젝트의 지원자리스트에(지원자 정보+인트로 추가)

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


    public void getMyApplyProjects() {

    }

    public void getMyConfirmProjects() {

    }

    public void getMyRejectProjects() {

    }

    public void getProjectApplicant() {

    }



}
