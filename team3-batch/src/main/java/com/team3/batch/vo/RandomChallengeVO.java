package com.team3.batch.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RandomChallengeVO {

    private Long memberId;
    private Long challengeId;

    @Builder
    public RandomChallengeVO(Long memberId, Long challengeId) {
        this.memberId = memberId;
        this.challengeId = challengeId;
    }
}
