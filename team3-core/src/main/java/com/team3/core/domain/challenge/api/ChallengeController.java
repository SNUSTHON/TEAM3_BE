package com.team3.core.domain.challenge.api;

import com.team3.core.domain.challenge.application.MyChallengeService;
import com.team3.core.domain.challenge.dto.response.DayChallengeResponse;
import com.team3.core.domain.challenge.dto.response.RangeChallengeResponse;
import com.team3.core.domain.challenge.dto.response.MyTodayChallengesResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.config.SwaggerConfig;
import com.team3.core.global.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "도전", description = "도전 관련 API")
@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final MyChallengeService myChallengeService;

    @GetMapping("/day")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "도전 과제 일별 조회 성공"
                    )
            }
    )
    @Operation(summary = "도전 과제 일별 조회", description = "도전 과제 일별 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<List<DayChallengeResponse>> getChallengesByDate(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date
    ) {
        List<DayChallengeResponse> response = myChallengeService.getChallengesByDate(team3OAuth2User.getId(), date);
        return StandardResponse.success(response);
    }

    @GetMapping("/range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "도전 과제 월별 조회 성공"
                    )
            }
    )
    @Operation(summary = "도전 과제 월별 조회", description = "도전 과제 월별 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<List<RangeChallengeResponse>> getChallengesByYearMonth(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<RangeChallengeResponse> response = myChallengeService.getChallengesCountByRange(team3OAuth2User.getId(), startDate, endDate);
        return StandardResponse.success(response);
    }

    @GetMapping("/today")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 도전 과제 전체 조회 성공"
                    )
            }
    )
    @Operation(summary = "오늘의 도전 과제 전체 조회", description = "오늘의 도전 과제 전체 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<List<MyTodayChallengesResponse>> getMyTodayChallenges(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        List<MyTodayChallengesResponse> response = myChallengeService.getMyTodayChallenges(team3OAuth2User.getId());
        return StandardResponse.success(response);
    }

    @PutMapping("/{challengeId}/status")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 도전 상태 변경 성공"
                    )
            }
    )
    @Operation(summary = "오늘의 도전 상태 변경", description = "오늘의 도전 상태 변경 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChallengeStatus(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @PathVariable Long challengeId,
            @RequestParam(required = true) boolean isDone
    ) {
        myChallengeService.updateStatus(team3OAuth2User.getId(), challengeId, isDone);
    }
}
