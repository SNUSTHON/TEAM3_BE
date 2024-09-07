package com.team3.core.domain.challenge.api;

import com.team3.core.domain.challenge.application.MyChallengeService;
import com.team3.core.domain.challenge.dto.response.DayChallengeResponse;
import com.team3.core.domain.challenge.dto.response.RangeChallengeResponse;
import com.team3.core.domain.challenge.dto.response.MyTodayChallengesResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.response.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final MyChallengeService myChallengeService;

    @GetMapping("/day")
    public StandardResponse<List<DayChallengeResponse>> getChallengesByDate(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date
    ) {
        List<DayChallengeResponse> response = myChallengeService.getChallengesByDate(team3OAuth2User.getId(), date);
        return StandardResponse.success(response);
    }

    @GetMapping("/range")
    public StandardResponse<List<RangeChallengeResponse>> getChallengesByYearMonth(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<RangeChallengeResponse> response = myChallengeService.getChallengesCountByRange(team3OAuth2User.getId(), startDate, endDate);
        return StandardResponse.success(response);
    }

    @GetMapping("/today")
    public StandardResponse<List<MyTodayChallengesResponse>> getMyTodayChallenges(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User
    ) {
        List<MyTodayChallengesResponse> response = myChallengeService.getMyTodayChallenges(team3OAuth2User.getId());
        return StandardResponse.success(response);
    }

    @PutMapping("/{challengeId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChallengeStatus(
            @AuthenticationPrincipal Team3OAuth2User team3OAuth2User,
            @PathVariable Long challengeId,
            @RequestParam(required = true) boolean isDone
    ) {
        myChallengeService.updateStatus(team3OAuth2User.getId(), challengeId, isDone);
    }
}
