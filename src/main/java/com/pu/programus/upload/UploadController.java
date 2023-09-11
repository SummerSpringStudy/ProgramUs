package com.pu.programus.upload;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    @PUTokenApiImplicitParams
    @PostMapping("/profile")
    public void uploadProfile(
            @RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
            @RequestParam MultipartFile image
            ) throws IOException {
        log.info("[uploadProfile] 프로필 이미지 업로드");

        String uid = jwtTokenProvider.getUid(token);
        log.info("[uploadProfile] uid: {}", uid);

        memberService.uploadProfile(uid, image.getBytes());
        log.info("[uploadProfile] 프로필 이미지 업로드 성공");
    }
}
