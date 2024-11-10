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
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CorsConfig corsConfig;
//    private final CorsFilter corsFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
//            .addFilterBefore(corsConfig.corsFilter(), SessionManagementFilter.class)
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
                    .requestMatchers("/api/v1/login", "/api/v1/register", "/api/v1/verify-email",
                        "/api/v1/send-email","/api/v1/test")
                    .permitAll()
                    .requestMatchers("/api/v1/member/**").hasAnyRole("MEMBER", "ADMIN")
                    .requestMatchers("/api/v1/character/**").hasAnyRole("MEMBER", "ADMIN")
                    .requestMatchers("/api/v1/list/**").hasAnyRole("MEMBER", "ADMIN")
                    .anyRequest().denyAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint))
        ;

        return http.build();
    }

    // CORS 설정을 위한 CorsConfigurationSource 빈 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*"); // 모든 Origin 허용
        configuration.addAllowedHeader("*"); // 모든 Header 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
