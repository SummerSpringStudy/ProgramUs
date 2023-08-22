package com.pu.programus.position;

import com.pu.programus.member.Member;
import com.pu.programus.project.ProjectHeadCount;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "position")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "position")
    private List<ProjectHeadCount> projectHeadCounts = new ArrayList<>();

    public Position(String name) {
        this.name = name;
    }

    public void addMember(Member member) {
        member.setPosition(this);
        members.add(member);
    }

    public void addProjectHeadCount(ProjectHeadCount projectHeadCount) {
        projectHeadCount.setPosition(this);
        projectHeadCounts.add(projectHeadCount);
    }
}
