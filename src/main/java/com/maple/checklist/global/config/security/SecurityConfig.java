package com.maple.checklist.global.config.security;

import com.maple.checklist.batch.MaintenanceFilter;
import com.maple.checklist.global.config.security.auth.CustomAccessDeniedHandler;
import com.maple.checklist.global.config.security.auth.CustomAuthenticationEntryPoint;
import com.maple.checklist.global.config.security.jwt.JwtAuthenticationFilter;
import com.maple.checklist.global.config.security.jwt.JwtExceptionFilter;
import com.maple.checklist.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
            .addFilterBefore(new MaintenanceFilter(), JwtExceptionFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                (sessionManagement) ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers("/login", "/register", "/verify-email").permitAll()
                    .requestMatchers("/api/v1/member/**").hasAnyRole("MEMBER", "ADMIN")
                    .requestMatchers("/api/v1/character/**").hasAnyRole("MEMBER", "ADMIN")
                    .requestMatchers("/api/v1/list/**").hasAnyRole("MEMBER", "ADMIN")
                    .anyRequest().permitAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint))
        ;

        return http.build();
    }
}
