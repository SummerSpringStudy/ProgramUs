package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.member.DTO.MemberDTO;
import com.pu.programus.member.dto.EditMemberDto;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.ProjectList;
import com.pu.programus.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PositionRepository positionRepository;

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

    //Todo: Exception 만들기??
    public void editMember(String uid, EditMemberDto editMemberDto) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

        //Todo: 괜찮은지??
        editMemberDto.editPrimitiveType(member);

        //Todo: 잘 반영되는지 체크
        editMemberPositionByPositionName(editMemberDto, member);

        memberRepository.save(member);
    }

    private void editMemberPositionByPositionName(EditMemberDto editMemberDto, Member member) {
        String positionName = editMemberDto.getPosition();
        Position position = positionRepository.findByName(positionName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Positon입니다."));

        member.setPosition(position);
    }
}
