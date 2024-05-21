package com.maple.checklist.global.config.log;

import com.maple.checklist.global.config.security.jwt.JwtTokenProvider;
import com.maple.checklist.global.utils.LogUtilsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {

    private final LogUtilsService logUtilsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        log.info("==================== BEGIN ====================");
        log.info("[REQUEST]");
        log.info("IP: {}",logUtilsService.getClientIpAddress(request));
        log.info("Request URI: {} {}", request.getMethod(), request.getRequestURI());
        log.info("Member Id: {}",getMemberId(request.getHeader("Authorization")));
        log.info("Request Body: {}",logUtilsService.readBody(request));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        log.info("[RESPONSE]");
        log.info("Response Status: {}", response.getStatus());
        log.info("==================== END ======================");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String getMemberId(String token) {
        if(jwtTokenProvider.minimumValidateToken(token)) {
            return jwtTokenProvider.getMemberIdByToken(token);
        } else {
            return "UNKNOWN";
        }
    }
}
