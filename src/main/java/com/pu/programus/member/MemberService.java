package com.pu.programus.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void modify(Long id, Member member) {
        if (!id.equals(member.getId()))
            throw new IllegalArgumentException();

        memberRepository.save(member);
    }

    public Member getProfile(String id) {
        Member member = memberRepository.findByUid(id).orElseThrow();

        return member;
    }
}
