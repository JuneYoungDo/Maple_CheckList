package com.maple.checklist.global.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Configuration
public class BcryptUtilsService {

    public String encrypt(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt());
    }

    public boolean isMatch(String pwd, String hashed) {
        return BCrypt.checkpw(pwd,hashed);
    }
}
