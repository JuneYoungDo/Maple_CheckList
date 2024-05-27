package com.maple.checklist.global.config.exception.errorCode;

import com.maple.checklist.global.config.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_JWT("AUTH_INVALID_JWT","Invalid JWT token.",HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("AUTH_EMPTY_JWT", "JWT token is empty.", HttpStatus.UNAUTHORIZED),
    EXPIRED_MEMBER_JWT("AUTH_EXPIRED_JWT", "JWT token has expired.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("AUTH_UNSUPPORTED_JWT", "Unsupported JWT token.", HttpStatus.UNAUTHORIZED),
    ALREADY_USED_EMAIL("EMAIL_DUPLICATION","The email address is already in use.",HttpStatus.CONFLICT),
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;
}
