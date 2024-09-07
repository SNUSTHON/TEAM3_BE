package com.team3.core.domain.challenge.dto.response;

import com.team3.core.domain.challenge.domain.Challenge;
import com.team3.core.domain.challenge.domain.MemberChallenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DayChallengeResponse {

    private String challengeName;
    private int level;
    private String categoryName;
    private int duration;
    private boolean isDone;

    @Builder
    public DayChallengeResponse(String challengeName, int level, String categoryName, int duration, boolean isDone) {
        this.challengeName = challengeName;
        this.level = level;
        this.categoryName = categoryName;
        this.duration = duration;
        this.isDone = isDone;
    }

    public static DayChallengeResponse from(MemberChallenge memberChallenge) {
        Challenge challenge = memberChallenge.getChallenge();

        return DayChallengeResponse.builder()
                .challengeName(challenge.getName())
                .level(challenge.getLevel())
                .categoryName(challenge.getCategory().getName())
                .duration(challenge.getDuration())
                .isDone(memberChallenge.isDone())
                .build();
    }
}
