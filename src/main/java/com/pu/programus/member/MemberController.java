package com.pu.programus.member;

import com.pu.programus.member.DTO.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * memberId의 마이 프로필을 가져오는 API
     *
     * @return 200 OK, 마이 프로필 정보
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<MemberDTO> getProfileById(@PathVariable String memberId) {
        log.info("[getProfileById] memberId: {}", memberId);
        MemberDTO member = memberService.getProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}
