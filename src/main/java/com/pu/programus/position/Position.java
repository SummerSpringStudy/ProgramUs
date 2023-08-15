package com.pu.programus.position;

import com.pu.programus.member.Member;
import com.pu.programus.project.ProjectHeadCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
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

}
