package com.team3.core.domain.auth.api;

import com.team3.core.domain.auth.dto.UserInfoDto;
import com.team3.core.domain.category.dto.MyCategoryResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.config.SwaggerConfig;
import com.team3.core.global.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "멤버", description = "멤버 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping
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
    public UserInfoDto getUserInfo(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        UserInfoDto userInfo = UserInfoDto.from(team3OAuth2User.getMember());
        return userInfo;
    }
}
