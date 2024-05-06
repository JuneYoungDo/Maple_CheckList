package com.maple.checklist.global.config.response;

import com.maple.checklist.global.config.exception.BaseException;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String code;
    private final String message;
    private final LocalDateTime time;


    public ExceptionResponse(BaseException e) {
        this.code = e.getErrorCode();
        this.message = e.getMessage();
        this.time = LocalDateTime.now();
    }
}
