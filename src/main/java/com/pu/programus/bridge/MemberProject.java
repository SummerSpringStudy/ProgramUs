package com.pu.programus.bridge;

import com.pu.programus.member.Member;
import com.pu.programus.project.Project;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class MemberProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Project project;
}
