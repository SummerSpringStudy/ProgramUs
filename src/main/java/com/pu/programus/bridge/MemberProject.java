package com.pu.programus.bridge;

import com.pu.programus.member.Member;
import com.pu.programus.position.Position;
import com.pu.programus.project.Project;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Project project;
}
