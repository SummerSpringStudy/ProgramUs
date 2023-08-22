package com.pu.programus.member.DTO;

import com.pu.programus.bridge.MemberProject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectMemberDTO {
    private String userName;
    private String position;

    public static ProjectMemberDTO make(MemberProject memberProject){
        return ProjectMemberDTO.builder()
                .userName(memberProject.getMember().getUsername())
                .position(memberProject.getMember().getPosition().getName())
                .build();
    }
}
