package com.maple.checklist.global.config.exception.errorCode;

import com.maple.checklist.global.config.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ListErrorCode implements ErrorCode {
    INVALID_LIST_ID("INVALID_LIST_ID","The list id is not exists", HttpStatus.BAD_REQUEST),
    INVALID_LIST("INVALID_LIST","The list is not yours",HttpStatus.BAD_REQUEST),
    INVALID_LIST_TYPE("INVALID_LIST_TYPE","The list type is not available",HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;
}
