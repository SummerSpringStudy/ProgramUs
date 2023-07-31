package com.pu.programus.project;

import com.pu.programus.MemberProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private Date startTime;
    private Date endTime;

    private ProjectStatus status;
    private String description;
    private String githubLink;

    @OneToMany
    private List<MemberProject> memberProject;
}
