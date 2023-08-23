package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.location.Location;
import com.pu.programus.member.DTO.ProjectMemberDTO;
import com.pu.programus.project.DTO.HeadCountResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<ProjectKeyword> projectKeywords = new ArrayList<>(); // 리스트로 만들필요 ex) 스프링 장고 -> project처럼 만들기

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    private Date startTime;
    private Date endTime;

    private ProjectStatus status;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProjectHeadCount> projectHeadCounts = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    private final List<MemberProject> memberProjects = new ArrayList<>();

    public void addProjectHeadCount(ProjectHeadCount projectHeadCount) {
        projectHeadCounts.add(projectHeadCount);
        projectHeadCount.setProject(this);
    }

    public void addMemberProject(MemberProject memberProject) {
        memberProjects.add(memberProject);
        memberProject.setProject(this);
    }

    public List<String> getKeywordValues(){
        return projectKeywords.stream().map(ProjectKeyword::getKeywordValue).collect(Collectors.toList());
    }

    public List<HeadCountResponseDTO> getHeadCounts(){
        return projectHeadCounts.stream()
                .map(HeadCountResponseDTO::make)
                .collect(Collectors.toList());
    }

    public List<ProjectMemberDTO> getProjectMembers(){
        return memberProjects.stream()
                .map(ProjectMemberDTO::make)
                .collect(Collectors.toList());
    }
}
