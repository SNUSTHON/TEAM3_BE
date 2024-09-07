package com.team3.core.domain.member.api;

import com.team3.core.domain.member.dto.response.ProfileResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.config.SwaggerConfig;
import com.team3.core.global.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "나의 정보 조회 성공",
                            useReturnTypeSchema = true
                    )
            }
    )
    @Operation(summary = "나의 정보 조회", description = "나의 정보 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<ProfileResponse> getProfile(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        ProfileResponse profile = ProfileResponse.from(team3OAuth2User.getMember());
        return StandardResponse.success(profile);
    }
}
