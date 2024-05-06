package com.maple.checklist.global.config.security;

import com.maple.checklist.global.config.security.auth.PrincipalDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PrincipalDetailService principalDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrfConfig) -> csrfConfig.disable())
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/login/**").permitAll()      // 메인 페이지와 로그인 관련 페이지 허용

            )
            .userDetailsService(principalDetailService)

        ;

        return http.build();
    }
}
