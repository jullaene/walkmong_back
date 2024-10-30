package org.jullaene.walkmong_back.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ResponseMessage body;

    public CustomException(HttpStatus httpStatus, ErrorType errorType) {
        super(errorType.getMessage());
        this.httpStatus = httpStatus;
        this.body = ResponseMessage.of(httpStatus, errorType.getMessage(), null);
    }
}
