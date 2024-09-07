package com.team3.core.domain.sponsor.api;

import com.team3.core.domain.sponsor.application.SponsorService;
import com.team3.core.domain.sponsor.dto.MySponsorResponse;
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

@Tag(name = "후원", description = "후원 관련 API")
@RestController
@RequestMapping("/api/sponsors")
@RequiredArgsConstructor
public class SponsorController {

    private final SponsorService sponsorService;

    @GetMapping("/my")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "나의 후원 증서 조회 성공",
                            useReturnTypeSchema = true
                    )
            }
    )
    @Operation(summary = "나의 후원 증서 조회", description = "나의 후원 증서 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<List<MySponsorResponse>> getMySponsors(@AuthenticationPrincipal Team3OAuth2User team3OAuth2User) {

        return StandardResponse.success(sponsorService.getMySponsors(team3OAuth2User.getId()));
    }

}
