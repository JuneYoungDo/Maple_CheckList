package com.maple.checklist.global.config.exception.errorCode;

import com.maple.checklist.global.config.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CharacterErrorCode implements ErrorCode {
    INVALID_NICKNAME("INVALID_NICKNAME","The nickname is not exists",HttpStatus.BAD_REQUEST),
    INVALID_CHARACTER_ID("INVALID_CHARACTER_ID","The character Id is not exists", HttpStatus.BAD_REQUEST),
    INVALID_CHARACTER("INVALID_CHARACTER","The character is noy yours",HttpStatus.BAD_REQUEST),
    ;


    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;
}
