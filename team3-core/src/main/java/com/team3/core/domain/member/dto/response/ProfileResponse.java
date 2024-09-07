package com.team3.core.domain.member.dto.response;

import com.team3.core.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {

    private Long memberId;
    private String email;
    private String username;
    private String profileImageUrl;
    private Double sponsorProgress;

    @Builder
    public ProfileResponse(Long memberId, String email, String username, String profileImageUrl, Double sponsorProgress) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.sponsorProgress = sponsorProgress;
    }

    public static ProfileResponse from(Member member) {
        return ProfileResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .profileImageUrl(member.getProfileImageUrl())
                .sponsorProgress(member.getSponsorProgress())
                .build();
    }
}
