package com.maple.checklist.global.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveEmailVerificationToken(String email, String token, long duration) {
        redisTemplate.opsForValue().set(email, token, duration, TimeUnit.MINUTES);
    }

    public String getEmailVerificationToken(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailVerificationToken(String email) {
        redisTemplate.delete(email);
    }

    public void saveEmailVerification(String email, long duration) {
        redisTemplate.opsForValue().set(email, true, duration, TimeUnit.MINUTES);
    }

    public Boolean getEmailVerification(String email) {
        return (Boolean) redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailVerification(String email) {
        redisTemplate.delete(email);
    }

    public void saveLogoutToken(String token, long duration) {
        redisTemplate.opsForValue().set(token, true, duration, TimeUnit.SECONDS);
    }

    public Boolean isLogoutToken(String token) {
        return (Boolean) redisTemplate.opsForValue().get(token);
    }
}
