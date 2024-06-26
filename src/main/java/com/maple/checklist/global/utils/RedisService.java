package com.maple.checklist.global.utils;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void validateKey(String key) {
        if (key == null || key.equals("")) {
            log.error("Redis key must not be null");
            throw new IllegalArgumentException("Key must not be null");
        }
    }

    public void saveEmailVerificationToken(String email, String token, long duration) {
        validateKey(email);
        redisTemplate.opsForValue().set(email, token, duration, TimeUnit.MINUTES);
    }

    public String getEmailVerificationToken(String email) {
        validateKey(email);
        return (String) redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailVerificationToken(String email) {
        validateKey(email);
        redisTemplate.delete(email);
    }

    public void saveEmailVerification(String email, long duration) {
        validateKey(email);
        redisTemplate.opsForValue().set(email, true, duration, TimeUnit.MINUTES);
    }

    public Boolean getEmailVerification(String email) {
        validateKey(email);
        return (Boolean) redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailVerification(String email) {
        validateKey(email);
        redisTemplate.delete(email);
    }

    public void saveLogoutToken(String token, long duration) {
        validateKey(token);
        redisTemplate.opsForValue().set(token, true, duration, TimeUnit.SECONDS);
    }

    public Boolean isLogoutToken(String token) {
        validateKey(token);
        return (Boolean) redisTemplate.opsForValue().get(token);
    }
}
