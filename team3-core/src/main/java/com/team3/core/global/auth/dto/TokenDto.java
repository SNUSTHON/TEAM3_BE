package com.team3.core.global.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private String accessToken;

    @Builder
    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
    }
}
