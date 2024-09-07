package com.team3.core.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberChallengeException extends RuntimeException {

    private final HttpStatus status;
    private final int code;

    public MemberChallengeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }
}