package com.pu.programus.projectApply;

import com.pu.programus.member.Member;
import com.pu.programus.position.Position;
import com.pu.programus.project.Project;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
@Entity@Builder
public class ProjectApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String intro;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    private ProjectApplyStatus projectApplyStatus;


}
