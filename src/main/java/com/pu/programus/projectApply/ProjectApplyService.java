package com.pu.programus.projectApply;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectHeadCount;
import com.pu.programus.project.ProjectRepository;
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

    /*
    public void apply(Long projectId, String positionName, String uid) {
        Project project = findProject(projectId);
        validateDuplicateApply(uid, project.getMemberProjects());
        updateHeadCountByPositionName(project, positionName);
        addMemberToProject(uid, project);
    }
    */

    public void apply(String uid) {

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


    public void getMyApplyProjects() {

    }

    public void getMyConfirmProjects() {

    }

    public void getMyRejectProjects() {

    }

    public void getProjectApplicant() {

    }



}
