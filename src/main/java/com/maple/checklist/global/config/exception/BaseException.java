package com.maple.checklist.global.config.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Getter
@Log4j2
public class BaseException extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final HttpStatus status;

    public BaseException(ErrorCode code) {
        errorCode = code.getErrorCode();
        message = code.getErrorMessage();
        status = code.getStatus();
        log.error("In BaseException ErrorCode: {}, ErrorMessage: {}, Status: {}", errorCode, message, status);
        log.info("==================== END ======================");
    }
}
