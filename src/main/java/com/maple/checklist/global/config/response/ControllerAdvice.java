package com.maple.checklist.global.config.response;

import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.ErrorCode;
import com.maple.checklist.global.config.exception.errorCode.GlobalErrorCode;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        return new ResponseEntity<>(new ExceptionResponse(e), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        String detailMessage = extractMessage(e.getBindingResult().getFieldErrors());
        ResponseEntity<ExceptionResponse> res = convert(GlobalErrorCode.NOT_VALID_ARGUMENT_ERROR,
            detailMessage);

        log.info("[EXCEPTION]");
        log.info("Response Status: {}", res.getStatusCode());
        log.info("Response Message: {}", detailMessage);
        log.info("==================== END ======================");

        return res;
    }

    private ResponseEntity<ExceptionResponse> convert(ErrorCode e, String detailMessage) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getErrorCode(), detailMessage);
        return new ResponseEntity<>(
            exceptionRes, HttpStatus.BAD_REQUEST);
    }

    private String extractMessage(List<FieldError> fieldErrors) {
        StringBuilder builder = new StringBuilder();
        fieldErrors.forEach((error) -> builder.append(error.getDefaultMessage()));
        return builder.toString();
    }
}
