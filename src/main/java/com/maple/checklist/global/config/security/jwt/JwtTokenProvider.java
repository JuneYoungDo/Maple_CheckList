package com.maple.checklist.global.config.security.jwt;

import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.AuthErrorCode;
import com.maple.checklist.global.config.security.auth.PrincipalDetailService;
import com.maple.checklist.global.config.security.auth.PrincipalDetails;
import com.maple.checklist.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;

    private final PrincipalDetailService principalDetailService;
    private final RedisService redisService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long TOKEN_VALID_TIME = 1000 * 60L * 60L * 6L;  // 유효기간 6시간

    @PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
    }

    public String generateAccessToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        Date now = new Date();
        Date accessTokenExpirationTime = new Date(now.getTime() + TOKEN_VALID_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(accessTokenExpirationTime)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = principalDetailService.loadUserByUsername(
                getMemberIdByToken(token));
            return new UsernamePasswordAuthenticationToken(principalDetails,
                "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new BaseException(AuthErrorCode.INVALID_JWT);
        }
    }

    public String getMemberIdByToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).
            getBody().get("memberId").toString();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    public boolean validateToken(String token) {
        Boolean flag = redisService.isLogoutToken(token);
        if (flag != null && flag) {
            throw new BaseException(AuthErrorCode.INVALID_JWT);
        }
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(AuthErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new BaseException(AuthErrorCode.EXPIRED_MEMBER_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new BaseException(AuthErrorCode.EMPTY_JWT);
        }
    }

    public long getRemainingValidityInSeconds(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecretKey)
            .parseClaimsJws(token)
            .getBody();

        Date expirationDate = claims.getExpiration();

        Date now = new Date();

        long remainingValidityInMillis = expirationDate.getTime() - now.getTime();
        long remainingValidityInSeconds = remainingValidityInMillis / 1000;

        if (remainingValidityInSeconds < 0) {
            return -1;
        }

        return remainingValidityInSeconds;
    }

    public boolean minimumValidateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
