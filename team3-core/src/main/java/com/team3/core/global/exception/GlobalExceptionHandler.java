package com.team3.core.global.exception;

import com.team3.core.global.response.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.team3.core.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);

        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();


        return ResponseEntity.status(e.getStatusCode()).body(StandardResponse.failure(e.getStatusCode().value(), errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleException(Exception e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity.internalServerError().body(StandardResponse.failure(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()));
    }
}
