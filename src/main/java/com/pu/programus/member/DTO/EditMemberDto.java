package com.pu.programus.member.DTO;

import com.pu.programus.location.Location;
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

    private String position;
}
