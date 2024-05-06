package com.maple.checklist.global.config.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();
    String getErrorMessage();
    HttpStatus getStatus();
}
