package com.maple.checklist.global.config.exception.errorCode;

import com.maple.checklist.global.config.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_JWT("AUTH_001","유효하지 않은 Jwt 입니다.",HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("AUTH_002", "Jwt가 없습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_MEMBER_JWT("AUTH_003", "만료된 Jwt입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("AUTH_004", "지원하지 않는 Jwt입니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;
}
