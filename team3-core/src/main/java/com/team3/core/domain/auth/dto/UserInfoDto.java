package com.team3.core.domain.auth.dto;

import com.team3.core.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoDto {

    private Long id;
    private String email;
    private String username;
    private String profileImage;

    @Builder
    public UserInfoDto(Long id, String email, String username, String profileImage) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static UserInfoDto from(Member member) {
        return UserInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .profileImage(member.getProfileImageUrl())
                .build();
    }
}
