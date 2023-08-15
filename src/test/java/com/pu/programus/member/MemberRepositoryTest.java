package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByUserId() {
        String 오정환 = "오 정 환";
        Member member1 = new Member();
        member1.setUserId(오정환);
        memberRepository.save(member1);

        Member member2 = memberRepository.findByUserId(오정환)
                .orElseThrow(() -> new IllegalArgumentException("Wrong userId"));
        Assertions.assertThat(member2.getUserId()).isEqualTo(member1.getUserId());
    }

    @Test
    void getProjectsFromMember() {
        /*
        String name = "TOM";
        List<MemberProject> projects = new ArrayList<>();
        projects.add(new MemberProject());
        projects.add(new MemberProject());

        Member member = new Member();
        member.setUserId(name);
        member.setMemberProject(projects);
        memberRepository.save(member);

        Member target = memberRepository.findByUserId(name)
                .orElseThrow(() -> new IllegalArgumentException("Wrong userId"));
        assertEquals(2, target.getMemberProject().size());
*/
    }
}