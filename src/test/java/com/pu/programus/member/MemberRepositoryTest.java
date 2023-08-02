package com.pu.programus.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByUserId() {
        String 오정환 = "오 정 환";
        Member member1 = new Member();
        member1.setUserId(오정환);
        memberRepository.save(member1);

        Member member2 = memberRepository.findByUserId(오정환).get();
        Assertions.assertThat(member2.getUserId()).isEqualTo(member1.getUserId());
    }
}