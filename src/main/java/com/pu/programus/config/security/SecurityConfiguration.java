package com.pu.programus.config.security;

import com.pu.programus.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${security.websecurity.path.permitall}")
    private String[] webSecurityPermitAllPath;
    @Value("${security.httpsecurity.path.permitall}")
    private String[] httpSecurityPermitAllPath;
    @Value("${security.httpsecurity.path.permituser}")
    private String[] httpSecurityPermitUserPath;
    @Value("${security.httpsecurity.path.permitadmin}")
    private String[] httpSecurityPermitAdminPath;

    public static final String TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String ROLES = "roles";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    //WebSecurity 후에 적용
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()

                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                .antMatchers(httpSecurityPermitAllPath).permitAll()

                .antMatchers(httpSecurityPermitUserPath).hasAnyRole(ROLE_ADMIN, ROLE_USER)

                .antMatchers(httpSecurityPermitAdminPath).hasRole(ROLE_ADMIN)

                .anyRequest().hasRole(ROLE_ADMIN)

                .and()
                .exceptionHandling().accessDeniedHandler(new BaseAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new BaseAuthenticationEntryPoint())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    //우선 적용
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(webSecurityPermitAllPath);
    }
}
