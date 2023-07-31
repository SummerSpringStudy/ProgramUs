package com.pu.programus;

import com.pu.programus.member.Member;
import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
public class MemberProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;
    @ManyToOne
    private Project project;
}
