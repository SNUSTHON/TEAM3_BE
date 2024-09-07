package com.team3.core.domain.challenge.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RangeChallengeResponse {

    private String createdAt;
    private int count;

    @Builder
    public RangeChallengeResponse(String createdAt, int count) {
        this.createdAt = createdAt;
        this.count = count;
    }
}
