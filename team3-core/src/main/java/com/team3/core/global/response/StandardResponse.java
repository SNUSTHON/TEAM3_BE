package com.team3.core.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // JSON에서 null 값인 필드 제외
public class StandardResponse<T> {

    private int code;
    private String message;
    private T data;

    public static <T> StandardResponse<T> success(String message) {
        return StandardResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> StandardResponse<T> success(T content) {
        return StandardResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message("API 요청 성공")
                .data(content)
                .build();
    }

    public static <T> StandardResponse<T> success(T content, String message) {
        return StandardResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(content)
                .build();
    }

    public static <T> StandardResponse<T> failure(int code, String message) {
        return StandardResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> StandardResponse<T> failure(int code, String message, T content) {
        return StandardResponse.<T>builder()
                .code(code)
                .message(message)
                .data(content)
                .build();
    }
}