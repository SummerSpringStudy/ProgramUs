package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.member.DTO.MemberDTO;
import com.pu.programus.project.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void modifyMemberInfo() {
        /*
        Long id = 1L;
        Member tmp = new Member();
        tmp.setId(id);
        tmp.setPassword("pw1234");
        tmp.setUid("tom123");
        tmp.setUserName("HongGilDong");
        memberRepository.save(tmp);

        Member target = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wrong id"));
        target.setUserName("김진수");

//        memberService.modify(id, target);

        Member user = memberRepository.findByUid(target.getUid())
                .orElseThrow(() -> new IllegalArgumentException("Wrong userId"));
        Assertions.assertEquals(user.getId(), target.getId());
         */
    }

    @Test
    public void getProfile() {
        /*
        // given
        Member member = Member.builder()
                .uid("tom123")
                .password("pw1234")
                .userName("HongGilDong")
                .build();
        Project project = Project.builder().title("Project1").build();
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        member.addMemberProject(memberProject);

        memberRepository.save(member);

        // when
        MemberDTO tom = memberService.getProfile("tom123");

        assertThat(tom).isNotNull();

         */
    }

}
