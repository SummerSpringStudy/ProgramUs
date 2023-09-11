package com.pu.programus.projectApply.DTO;

import com.pu.programus.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Builder
@AllArgsConstructor
public class ProjectApplicantDTO {
    private Member member;
    private String position;
    private String intro;
}
