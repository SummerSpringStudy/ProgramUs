package com.pu.programus.config.security;

import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BaseUserDetailsService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberRepository.class);

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
        Optional<Member> optionalMember = memberRepository.findByUid(username);
        if (optionalMember.isEmpty())
            throw new UsernameNotFoundException(username);

        UserDetails userDetails = memberRepository.findByUid(username).get();
        return userDetails;
    }
}
