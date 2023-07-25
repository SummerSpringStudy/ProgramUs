package com.pu.programus.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
