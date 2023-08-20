package com.pu.programus.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity

public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .defaultSuccessUrl("view/dashboard", true)
                    .permitAll()
                .and()
                    .logout();

         */
    }

    /**

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("로그인없이 보여줄 화면 경로 ex메인첫화면");
    }
    **/
}
