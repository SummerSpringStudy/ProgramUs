package com.pu.programus.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void modifyMemberInfo() {
        Long id = 1L;
        Member tmp = new Member();
        tmp.setId(id);
        tmp.setUserName("HongGilDong");
        memberRepository.save(tmp);

        Member target = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wrong id"));
        target.setUserName("김진수");

        memberService.modify(id, target);

        Member user = memberRepository.findByUid(target.getUid())
                .orElseThrow(() -> new IllegalArgumentException("Wrong userId"));
        Assertions.assertEquals(user.getId(), target.getId());
    }

}
