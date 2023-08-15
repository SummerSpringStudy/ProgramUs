package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.member.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectHeadCountRepository projectHeadCountRepository;

    public void create(Member member) {

    }

    public void modify(Long id, Member member) {

    }

    public void show(Long id, Member member) {

    }

    public void saveProject(Project project) {
        //Todo: keywords도 추가하기
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
}
