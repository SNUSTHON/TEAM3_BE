package com.team3.core.global.exception;

import com.team3.core.global.response.StandardResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 멤버입니다."),
    MEMBER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, 3001, "존재하지 않는 도전 과제입니다."),
    CATEGORY_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, 4001, "존재하지 않는 카테고리 레벨입니다."),
    SPONSOR_NOT_FOUND(HttpStatus.NOT_FOUND, 4501, "존재하지 않는 후원 정보입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public StandardResponse<Void> getErrorResponse() {
        return StandardResponse.failure(code, message);
    }
}
