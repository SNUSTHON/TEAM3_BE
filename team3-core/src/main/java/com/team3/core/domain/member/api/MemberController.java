package com.team3.core.domain.member.api;

import com.team3.core.domain.member.dto.response.ProfileResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.response.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/my")
    public StandardResponse<ProfileResponse> getProfile(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        ProfileResponse profile = ProfileResponse.from(team3OAuth2User.getMember());
        return StandardResponse.success(profile);
    }
}
