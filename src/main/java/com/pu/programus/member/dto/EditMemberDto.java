package com.pu.programus.member.dto;

import com.pu.programus.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class EditMemberDto {
    private String password;
    private String userName;
    private String department;
    private String email;
    private String intro;
    private String contents;

    //Todo: 처리하기
    private String position;

    //Todo: 논의 사항
    public void editPrimitiveType(Member member) {
        //Todo: 비밀번호도 일괄 수정??
        member.setPassword(password);
        member.setUserName(userName);
        member.setDepartment(department);
        member.setEmail(email);
        member.setIntro(intro);
        member.setContents(contents);
    }
}
