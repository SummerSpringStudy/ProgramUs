package com.pu.programus.member.DTO;

import com.pu.programus.position.DTO.PositionDTO;
import com.pu.programus.project.DTO.ProjectList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MemberDTO {
    private String uid; // 아이디
    private String userName; // 닉네임
    private String department; // 소속
    private String email; // 이메일
    private String intro; // 소개
    private String contents; // 본문 소개
    private PositionDTO position; // 카테고리로 바꾸기
    private ProjectList projectList;
}
