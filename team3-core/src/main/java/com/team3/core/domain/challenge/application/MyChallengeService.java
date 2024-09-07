package com.team3.core.domain.challenge.application;

import com.team3.core.domain.category.dao.CategoryLevelRepository;
import com.team3.core.domain.category.domain.Category;
import com.team3.core.domain.category.domain.CategoryLevel;
import com.team3.core.domain.challenge.dao.MemberChallengeQueryRepository;
import com.team3.core.domain.challenge.dao.MemberChallengeRepository;
import com.team3.core.domain.challenge.domain.MemberChallenge;
import com.team3.core.domain.challenge.dto.response.DayChallengeResponse;
import com.team3.core.domain.challenge.dto.response.MyTodayChallengesResponse;
import com.team3.core.domain.challenge.dto.response.RangeChallengeResponse;
import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.exception.CategoryLevelException;
import com.team3.core.global.exception.ErrorCode;
import com.team3.core.global.exception.MemberChallengeException;
import com.team3.core.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.team3.core.global.common.Constants.SPONSOR_PROGRESS_INCREASE_RATE;

@Service
@RequiredArgsConstructor
public class MyChallengeService {

    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberChallengeQueryRepository memberChallengeQueryRepository;
    private final CategoryLevelRepository categoryLevelRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MyTodayChallengesResponse> getMyTodayChallenges(Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startAt = today.atStartOfDay();
        LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 59, 59, 999999999));
        List<MemberChallenge> todayMemberChallenges = memberChallengeRepository
                .findMemberChallengesByMemberIdAndDate(memberId, startAt, endAt);

        // category.name을 기준으로 분류
        Map<String, List<MemberChallenge>> groupedByCategoryName = todayMemberChallenges.stream()
                .collect(Collectors.groupingBy(mc -> mc.getChallenge().getCategory().getName()));

        // DTO 리스트로 변환
        return groupedByCategoryName.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<MyTodayChallengesResponse.MyTodayChallengeItem> challenges = entry.getValue().stream()
                            .map(MyTodayChallengesResponse.MyTodayChallengeItem::from)
                            .collect(Collectors.toList());

                    return MyTodayChallengesResponse.of(categoryName, challenges);
                })
                .toList();
    }

    @Transactional
    public void updateStatus(Long memberId, Long challengeId, boolean isDone) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        MemberChallenge findMemberChallenge = memberChallengeRepository.findByIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeException(ErrorCode.MEMBER_CHALLENGE_NOT_FOUND));

        findMemberChallenge.updateStatus(isDone);

        Category findCategory = findMemberChallenge.getChallenge().getCategory();
        CategoryLevel findCategoryLevel = categoryLevelRepository.findByMemberIdAndCategoryId(memberId, findCategory.getId())
                .orElseThrow(() -> new CategoryLevelException(ErrorCode.CATEGORY_LEVEL_NOT_FOUND));

        if (isDone) { // 보상 지급 로직
            // 카테고리 레벨 증가
            findCategoryLevel.plusCnt();

            // 후원 진척도 레벨 증가
            boolean isFull = findMember.increaseSponsorProgressAndCheckIsFull(SPONSOR_PROGRESS_INCREASE_RATE);

            if (isFull) {
                // TODO: 후원 객체 생성
            }

        } else { // 보상 지급 철회
            // 카테고리 레벨 감소
            findCategoryLevel.minusCnt();

            // 후원 진척도 레벨 감소
            boolean isCanceled = findMember.decreaseSponsorProgressAndCheckIsCanceled(SPONSOR_PROGRESS_INCREASE_RATE);

            // 후원 객체 생성되었던 경우, 삭제
            if (isCanceled) {
                // TODO: 마지막으로 생성된 후원 객체 삭제
            }
        }


    }

    @Transactional(readOnly = true)
    public List<DayChallengeResponse> getChallengesByDate(Long memberId, String dateFormatString) {
        return memberChallengeQueryRepository.findChallengesByDate(memberId, dateFormatString);
    }

    @Transactional(readOnly = true)
    public List<RangeChallengeResponse> getChallengesCountByRange(Long memberId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startAt = startDate.atStartOfDay();
        LocalDateTime endAt = LocalDateTime.of(
                endDate,
                LocalTime.of(23, 59, 59, 999999999)
        );

        return memberChallengeQueryRepository.findChallengesCountByRange(memberId, startAt, endAt);
    }
}
