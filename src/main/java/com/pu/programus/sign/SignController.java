package com.pu.programus.sign;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.exception.SignException;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.sign.dto.SignInDto;
import com.pu.programus.sign.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;
    private final JwtTokenProvider jwtTokenProvider;

    @PUTokenApiImplicitParams
    @GetMapping(value = "/token/refresh")
    public String refreshToken(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) throws SignException {
        String uid = jwtTokenProvider.getUid(token);

        return signService.makeTokenByUid(uid);
    }

    @PostMapping(value = "/sign-up")
    public void signUp(
            @Validated @RequestBody SignUpDto signUpDto) throws SignException{
        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}, pw : ****, name : {}", signUpDto.getId(), signUpDto.getName());
        signService.signUp(signUpDto);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", signUpDto.getId());
    }

    /**
     * @param signInDto
     * @return token
     * @throws SignException
     */
    @PostMapping(value = "/sign-in")
    public String signIn(
            @Validated @RequestBody SignInDto signInDto) throws SignException {
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signInDto.getId());
        String token = signService.signIn(signInDto);

        return token;
    }

    @PUTokenApiImplicitParams
    @DeleteMapping(value = "/sign-out")
    public void signOut(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) throws SignException {
        String uid = jwtTokenProvider.getUid(token);
        LOGGER.info("[signIn] 회원탈퇴를 시도하고 있습니다. id : {}, pw : ****", uid);

        signService.signOut(uid);

        LOGGER.info("[signIn] 정상적으로 회원 탈퇴 되었습니다. id : {}, token : {}", uid);
    }
}
