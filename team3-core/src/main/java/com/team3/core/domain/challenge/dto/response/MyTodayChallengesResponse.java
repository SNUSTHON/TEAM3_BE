package com.team3.core.domain.challenge.dto.response;

import com.team3.core.domain.challenge.domain.MemberChallenge;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyTodayChallengesResponse {

    private String categoryName;

    private List<MyTodayChallengeItem> challenges;

    public MyTodayChallengesResponse(String categoryName, List<MyTodayChallengeItem> challenges) {
        this.categoryName = categoryName;
        this.challenges = challenges;
    }

    public static MyTodayChallengesResponse of(String categoryName, List<MyTodayChallengeItem> challenges) {
        return new MyTodayChallengesResponse(categoryName, challenges);
    }

    @Getter
    @NoArgsConstructor
    public static class MyTodayChallengeItem {
        private Long memberChallengesId;
        private String challengeName;
        private int level;
        private int duration;
        private boolean isDone;

        @Builder
        public MyTodayChallengeItem(Long memberChallengesId, String challengeName, int level, int duration, boolean isDone) {
            this.memberChallengesId = memberChallengesId;
            this.challengeName = challengeName;
            this.level = level;
            this.duration = duration;
            this.isDone = isDone;
        }

        public static MyTodayChallengeItem from(MemberChallenge memberChallenge) {
            return MyTodayChallengeItem.builder()
                    .memberChallengesId(memberChallenge.getId())
                    .challengeName(memberChallenge.getChallenge().getName())
                    .level(memberChallenge.getChallenge().getLevel())
                    .duration(memberChallenge.getChallenge().getDuration())
                    .isDone(memberChallenge.isDone())
                    .build();
        }
    }
}


