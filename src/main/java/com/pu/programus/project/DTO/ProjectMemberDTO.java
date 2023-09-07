package com.pu.programus.project.DTO;

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

    // 프로젝트 생성 과정에서 오너가 본인의 포지션을 선택하는 과정 필요?
    public static ProjectMemberDTO make(MemberProject memberProject){
        return ProjectMemberDTO.builder()
                .userName(memberProject.getMember().getUsername())
                .position(memberProject.getPosition().getName())
                .build();
    }
}
