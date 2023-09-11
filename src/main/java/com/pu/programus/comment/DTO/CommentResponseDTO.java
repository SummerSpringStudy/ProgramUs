package com.pu.programus.comment.DTO;


import com.pu.programus.comment.Comment;
import com.pu.programus.member.Member;
import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static io.swagger.models.properties.PropertyBuilder.build;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private String comment;
    private Long projectId;
    private Long memberId;
    private boolean isSecret;

    public static CommentResponseDTO make(Comment comment){

        Project project = comment.getProject();

        if(project == null){
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }

        Member member = comment.getMember();

        if(member == null){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        return CommentResponseDTO.builder()
                .comment(comment.getComment())
                .projectId(project.getId())
                .memberId(member.getId())
                .isSecret(comment.isSecret())
                .build();
    }
}
