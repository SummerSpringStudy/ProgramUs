package com.pu.programus.member;

import com.pu.programus.MemberProject;
import com.pu.programus.position.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // key

    private String email;
    private String userId; // 아이디
    private String userPw; // 패스워드
    private String userName; // 닉네임
    private String department; // 소속
    private String intro; // 소개
    private String contents; // 본문 소개

    @ManyToOne
    private Position position; // 카테고리로 바꾸기

    @OneToMany(mappedBy = "project") // mappedBy로 참조하는 외래키임을 명시
    private List<MemberProject> memberProject;
}
