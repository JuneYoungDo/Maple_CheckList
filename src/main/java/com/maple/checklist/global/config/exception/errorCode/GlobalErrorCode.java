package com.maple.checklist.global.config.exception.errorCode;

import com.maple.checklist.global.config.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    NOT_VALID_ARGUMENT_ERROR("INVALID_ARGUMENT","Invalid Argument is exist",HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_URI_ERROR("INVALID_URI","The requested URI was not found on this server.",HttpStatus.NOT_FOUND),
    NOT_SUPPORTED_METHOD_ERROR("UNSUPPORTED_METHOD","The requested HTTP method is not supported for this resource.",HttpStatus.METHOD_NOT_ALLOWED),
    NOT_SUPPORTED_MEDIA_TYPE_ERROR("UNSUPPORTED_MEDIA_TYPE","The media type of the requested data is not supported.",HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    ACCESS_DENIED("ACCESS_DENIED","You do not have permission to access this resource.",HttpStatus.FORBIDDEN),
    SERVER_ERROR("SERVER_ERROR","The server encountered an internal error and could not complete your request. Please contact the server administrator if the issue persists.",HttpStatus.INTERNAL_SERVER_ERROR),

        ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;
}
