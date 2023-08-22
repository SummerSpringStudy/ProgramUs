package com.pu.programus.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * memberId의 마이 프로필을 가져오는 API
     *
     * @return 200 OK, 마이 프로필 정보
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Member> getProfileById(@PathVariable String memberId) {
        Member member = memberService.getProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}
