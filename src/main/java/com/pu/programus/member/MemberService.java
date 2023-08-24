package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.location.Location;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.DTO.MemberDTO;
import com.pu.programus.member.DTO.EditMemberDto;
import com.pu.programus.position.DTO.PositionDTO;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.DTO.ProjectDTO;
import com.pu.programus.project.DTO.ProjectList;
import com.pu.programus.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;

    public MemberDTO getProfile(String id) {
        Member member = memberRepository.findByUid(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

        log.info("[getProfile] Member: {}", member);

        ProjectList projectList = getProjects(member);
        log.info("[getProfile] ProjectList: {}", projectList);

        Position position = member.getPosition();
        String postionName = "";
        if (position != null)
            postionName = position.getName();

        // Todo: 디폴트 포지션 만들기
        PositionDTO positionDTO = new PositionDTO(postionName);
        MemberDTO memberDTO = MemberDTO.builder()
                .uid(member.getUid())
                .userName(member.getUsername())
                .intro(member.getIntro())
                .email(member.getEmail())
                .department(member.getDepartment())
                .contents(member.getContents())
                .position(positionDTO)
                .projectList(projectList)
                .build();
        log.info("[getProfile] memberDTO: {}", memberDTO);
        return memberDTO;
    }

    private static ProjectList getProjects(Member member) {
        List<ProjectDTO> projects = new ArrayList<>();
        for (MemberProject mp : member.getMemberProjects()) {
            Project project = mp.getProject();
            ProjectDTO dto = ProjectDTO.builder().title(project.getTitle()).description(project.getDescription()).build();
            projects.add(dto);
        }
        ProjectList projectList = new ProjectList(projects);
        return projectList;
    }

    //Todo: Exception 만들기??
    public void editMember(String uid, EditMemberDto editMemberDto) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));

        //Todo: 괜찮은지??
        editMemberDto.editPrimitiveType(member);

        //Todo: 잘 반영되는지 체크
        editPosition(editMemberDto, member);

        memberRepository.save(member);
    }

    private void editPosition(EditMemberDto editMemberDto, Member member) {
        Position position = positionRepository.findByName(editMemberDto.getPosition())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Position 입니다."));
        member.setPosition(position);
    }
}
