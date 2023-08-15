package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.bridge.ProjectKeywordRepository;
import com.pu.programus.member.Member;
import com.pu.programus.position.Position;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectHeadCountRepository projectHeadCountRepository;
    private final ProjectKeywordRepository projectKeywordRepository;

    public void create(Member member) {

    }

    public void modify(Long id, Member member) {

    }

    public void show(Long id, Member member) {

    }
    //Todo: projectHeadCount 생성 로직 만들기

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

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public List<Project> findProjectsByRecruitingPosition(Position pos) {
        List<Project> projects = projectRepository.findAll();
        List<Project> result = new ArrayList<>();

        for (Project project : projects) {
            List<ProjectHeadCount> projectHeadCounts = project.getProjectHeadCounts();
            for (ProjectHeadCount projectHeadCount : projectHeadCounts) {
                if (projectHeadCount.getPosition().equals(pos)) {
                    result.add(project);
                    break;
                }
            }
        }
        return result;
    }
    public List<Project> getProjectsByTitle(String title) {
        return projectRepository.findByTitle(title);
    }
}
