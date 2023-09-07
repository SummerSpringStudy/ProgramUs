package com.pu.programus.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByUserId() {
        String name = "오 정 환";
        Member member1 = Member.builder()
                .uid(name)
                .userName("OJH")
                .password("1234")
                .build();

        memberRepository.save(member1);

        Member member2 = memberRepository.findByUid(name)
                .orElseThrow(() -> new IllegalArgumentException("Wrong userId"));
        Assertions.assertThat(member2.getUid()).isEqualTo(member1.getUid());
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