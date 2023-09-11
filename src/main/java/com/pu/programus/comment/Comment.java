package com.pu.programus.comment;

import com.pu.programus.member.Member;
import com.pu.programus.project.Project;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private boolean isSecret;

    public void toSecret(){
        setComment("비밀댓글 입니다.");
    }
}
