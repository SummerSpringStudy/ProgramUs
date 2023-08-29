package com.pu.programus.bridge;

import com.pu.programus.location.Location;
import com.pu.programus.location.LocationRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.position.Position;
import com.pu.programus.position.PositionRepository;
import com.pu.programus.project.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberProjectRepositoryTest {
    @Autowired
    MemberProjectRepository memberProjectRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void MemberProjectRepositoryTest() {
        /*
        // given
        Location location = new Location("123");
        locationRepository.save(location);

        Position position1 = new Position("11");
        positionRepository.save(position1);

        MemberProject memberProject = new MemberProject();
        Member member = Member.builder()
                .uid("q")
                .password("1")
                .userName("123")
                .build();
        Position position = new Position();
        Project project = Project.builder()
                .location(location)
                .build();
        memberProject.setProject(project);

        member.addMemberProject(memberProject);
        project.addMemberProject(memberProject);
        memberProjectRepository.save(memberProject);

        // when
        MemberProject memberProject1 = memberProjectRepository.findByMemberIdAndProjectId(member.getId(), project.getId()).orElseThrow(() -> new IllegalArgumentException("못찾음"));

        // then
        Assertions.assertNotNull(memberProject1);
        memberProjectRepository.deleteByMemberIdAndProjectId(member.getId(), project.getId());

        Optional<MemberProject> byMemberIdAndProjectId = memberProjectRepository.findByMemberIdAndProjectId(member.getId(), project.getId());
        System.out.println(byMemberIdAndProjectId.get());

        Member q = memberRepository.findByUid("q").orElseThrow(()->new IllegalArgumentException("멤버 못찾음"));
*/
    }
}