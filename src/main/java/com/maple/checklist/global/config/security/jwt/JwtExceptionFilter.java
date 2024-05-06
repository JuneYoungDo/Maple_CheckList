package com.maple.checklist.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.checklist.global.config.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException exception) {
            setErrorResponse(response, exception);
        }
    }

    public void setErrorResponse(HttpServletResponse response, BaseException exception)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(exception.getStatus().value());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", exception.getErrorCode());
        responseBody.put("message", exception.getMessage());
        responseBody.put("timestamp", LocalDateTime.now().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
