package com.team3.core.domain.member.domain;

import com.team3.core.domain.challenge.domain.MemberChallenge;
import com.team3.core.global.auth.model.OAuth2Provider;
import com.team3.core.global.auth.model.Role;
import com.team3.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static com.team3.core.global.common.Constants.MAX_SPONSOR_PROGRESS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Column(unique = true)
    private String providerId;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("0")
    private double sponsorProgress = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberChallenge> memberChallenges = new ArrayList<>();

    @Builder
    public Member(String username, String email, OAuth2Provider provider, String providerId, String profileImageUrl, Role role) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    public boolean increaseSponsorProgressAndCheckIsFull(double value) {
        this.sponsorProgress += value;

        if (sponsorProgress >= MAX_SPONSOR_PROGRESS) {
            resetSponsorProgress();
            return true;
        }

        return false;
    }

    public boolean decreaseSponsorProgressAndCheckIsCanceled(double value) {
        double diff = value - this.sponsorProgress;
        if (diff > 0) {
            this.sponsorProgress = MAX_SPONSOR_PROGRESS - diff;
            return true;
        } else {
            this.sponsorProgress -= value;
            return false;
        }
    }

    public void resetSponsorProgress() {
        this.sponsorProgress = 0;
    }

    /* 연관관계 메서드 */
    public void addMemberChallenges(MemberChallenge memberChallenge) {
        this.memberChallenges.add(memberChallenge);
        memberChallenge.setMember(this);
    }
}
