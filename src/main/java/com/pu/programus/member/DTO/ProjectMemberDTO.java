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

    // 프로젝트 생성 과정에서 오너가 본인의 포지션을 선택하는 과정 필요?
    // 멤버가 프로젝트에서 무슨 포지션으로 참여했는지 알 수 있는 방법이 현재 없음
    public static ProjectMemberDTO make(MemberProject memberProject){
        return ProjectMemberDTO.builder()
                .userName(memberProject.getMember().getUsername())
                .position("미정")
                .build();
    }
}
