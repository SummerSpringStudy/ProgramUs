package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.member.DTO.MemberDTO;
import com.pu.programus.project.DTO.ProjectList;
import com.pu.programus.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void modify(Long id, Member member) {
        if (!id.equals(member.getId()))
            throw new IllegalArgumentException();

        memberRepository.save(member);
    }

    public MemberDTO getProfile(String id) {
        Member member = memberRepository.findByUid(id).orElseThrow();
        ProjectList projectList = getProjects(member);
        MemberDTO memberDTO = MemberDTO.builder()
                .uid(member.getUid())
                .userName(member.getUsername())
                .intro(member.getIntro())
                .email(member.getEmail())
                .department(member.getDepartment())
                .contents(member.getContents())
                .position(member.getPosition())
                .projectList(projectList)
                .build();
        return memberDTO;
    }

    private static ProjectList getProjects(Member member) {
        List<Project> projects = new ArrayList<>();
        for (MemberProject mp : member.getMemberProjects()) {
            projects.add(mp.getProject());
        }
        ProjectList projectList = new ProjectList(projects);
        return projectList;
    }
}
