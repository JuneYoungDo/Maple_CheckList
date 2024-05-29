package com.maple.checklist.global.config.response;

import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.ErrorCode;
import com.maple.checklist.global.config.exception.errorCode.GlobalErrorCode;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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

        log.error("[EXCEPTION]");
        log.error("Response Status: {}", res.getStatusCode());
        log.error("Response Message: {}", detailMessage);
        log.info("==================== END ======================");

        return res;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(
        NoHandlerFoundException e) {
        return convert(GlobalErrorCode.NOT_SUPPORTED_URI_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        return convert(GlobalErrorCode.NOT_SUPPORTED_METHOD_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException e) {
        return convert(GlobalErrorCode.NOT_SUPPORTED_MEDIA_TYPE_ERROR);
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
//        return convert(GlobalErrorCode.SERVER_ERROR);
//    }

    private ResponseEntity<ExceptionResponse> convert(ErrorCode e) {
        ResponseEntity<ExceptionResponse> res = new ResponseEntity<>(new ExceptionResponse(e.getErrorCode(), e.getErrorMessage()), e.getStatus());
        log.error("[EXCEPTION]");
        log.error("Response Status: {}", res.getStatusCode());
        log.error("Response Message: {}", e.getErrorMessage());
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
