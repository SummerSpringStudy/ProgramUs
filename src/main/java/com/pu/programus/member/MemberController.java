package com.pu.programus.member;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.member.DTO.EditMemberDto;
import com.pu.programus.member.DTO.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * memberId의 마이 프로필을 가져오는 API
     *
     * @return 200 OK, 마이 프로필 정보
     */


    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getProfileById(@PathVariable String memberId) {
        log.info("[getProfileById] memberId: {}", memberId);
        MemberDTO member = memberService.getProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PUTokenApiImplicitParams
    @PostMapping("/edit")
    public void editMember(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestBody EditMemberDto editMemberDto) {
        String uid = jwtTokenProvider.getUid(token);

        memberService.editMember(uid, editMemberDto);
    }
}
