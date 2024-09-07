package com.team3.core.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {

    private final HttpStatus status;
    private final int code;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }
}