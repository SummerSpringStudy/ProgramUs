package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.ProjectKeyword;
import com.pu.programus.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "project")
    private List<ProjectKeyword> projectKeywords = new ArrayList<>(); // 리스트로 만들필요 ex) 스프링 장고 -> project처럼 만들기

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Location location;

    private Date startTime;
    private Date endTime;

    private ProjectStatus status;
    private String description;

    @OneToMany(mappedBy = "project")
    private List<ProjectHeadCount> projectHeadCounts = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<MemberProject> memberProjects = new ArrayList<>();

    public void addProjectHeadCount(ProjectHeadCount projectHeadCount) {
        projectHeadCounts.add(projectHeadCount);
        projectHeadCount.setProject(this);
    }
}
