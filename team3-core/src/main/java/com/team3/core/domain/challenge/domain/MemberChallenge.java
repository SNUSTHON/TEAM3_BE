package com.team3.core.domain.challenge.domain;

import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChallenge extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", referencedColumnName = "id")
    private Challenge challenge;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean isDone = false;

    @Builder
    public MemberChallenge(Member member, Challenge challenge) {
        this.member = member;
        this.challenge = challenge;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void updateStatus(boolean isDone) {
        this.isDone = isDone;
    }
}
