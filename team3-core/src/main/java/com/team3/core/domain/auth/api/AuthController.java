package com.team3.core.domain.auth.api;

import com.team3.core.domain.auth.dto.UserInfo;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.response.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping
    public StandardResponse<UserInfo> getUserInfo(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        UserInfo userInfo = UserInfo.from(team3OAuth2User.getMember());
        return StandardResponse.success(userInfo);
    }
}
