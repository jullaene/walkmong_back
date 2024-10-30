package org.jullaene.walkmong_back.common.exception;

import java.io.IOException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseMessage> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ResponseMessage errorResponse = new ResponseMessage(ErrorType.REQUEST_VALIDATION_ERROR.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseMessage errorResponse = new ResponseMessage(ex.getMessage(), status.value(), null);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSqlException(SQLException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseMessage errorResponse = new ResponseMessage("[SQL ERROR]" + ex.getMessage(), status.value(), null);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
