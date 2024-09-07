package com.team3.core.domain.challenge.application;

import com.team3.core.domain.category.dao.CategoryLevelRepository;
import com.team3.core.domain.category.dao.CategoryRepository;
import com.team3.core.domain.category.domain.Category;
import com.team3.core.domain.category.domain.CategoryLevel;
import com.team3.core.domain.challenge.dao.ChallengeRepository;
import com.team3.core.domain.challenge.dao.MemberChallengeRepository;
import com.team3.core.domain.challenge.domain.Challenge;
import com.team3.core.domain.challenge.domain.MemberChallenge;
import com.team3.core.domain.challenge.dto.response.DayChallengeResponse;
import com.team3.core.domain.challenge.dto.response.MyTodayChallengesResponse;
import com.team3.core.domain.challenge.dto.response.RangeChallengeResponse;
import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.auth.model.OAuth2Provider;
import com.team3.core.global.auth.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("[Service] - MyChallengeService")
@Transactional
class MyChallengeServiceTest {

    @Autowired
    MyChallengeService myChallengeService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryLevelRepository categoryLevelRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    MemberChallengeRepository memberChallengeRepository;

    List<Category> categories;

    @BeforeEach
    void setUp() {
        categories = createCategories();
    }

    @Test
    @DisplayName("getMyTodayChallenges")
    void getMyTodayChallenges() {
        // given
        Member member = createMember();
        List<List<MemberChallenge>> list = IntStream.range(0, 3)
                .mapToObj(i -> {
                    List<Challenge> challenges = createChallengesByCategories(10, categories.get(i));
                    List<MemberChallenge> memberChallenges = challenges.stream().map(c -> MemberChallenge.builder()
                            .challenge(c)
                            .member(member)
                            .build()
                    ).toList();
                    return memberChallengeRepository.saveAll(memberChallenges);
                }).toList();


        // when
        List<MyTodayChallengesResponse> myTodayChallenges = myChallengeService.getMyTodayChallenges(member.getId());

        // then
        assertThat(myTodayChallenges)
                .hasSize(3);
    }


    @Test
    @DisplayName("updateStatus_true")
    void updateStatus_true() {
        // given
        Member member = createMember();

        List<Challenge> challenges = createChallengesByCategories(1, categories.get(0));
        MemberChallenge memberChallenge = MemberChallenge.builder()
                .challenge(challenges.get(0))
                .member(member)
                .build();
        memberChallengeRepository.save(memberChallenge);

        CategoryLevel categoryLevel = CategoryLevel.builder()
                .cnt(0)
                .level(1)
                .member(member)
                .category(categories.get(0))
                .isSelected(true)
                .build();
        categoryLevelRepository.save(categoryLevel);

        // when
        myChallengeService.updateStatus(member.getId(), memberChallenge.getId(), true);

        // then
        CategoryLevel findCategoryLevel = categoryLevelRepository.findByMemberIdAndCategoryId(member.getId(), memberChallenge.getChallenge().getCategory().getId()).get();
        MemberChallenge findMemberChallenge = memberChallengeRepository.findById(memberChallenge.getId()).get();
        assertThat(findMemberChallenge.isDone()).isTrue();
        assertThat(member.getSponsor_progress()).isEqualTo(0.1);
        assertThat(findCategoryLevel.getLevel()).isEqualTo(1);
        assertThat(findCategoryLevel.getCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("updateStatus_false")
    void updateStatus_false() {
        // given
        Member member = createMember();

        List<Challenge> challenges = createChallengesByCategories(1, categories.get(0));
        MemberChallenge memberChallenge = MemberChallenge.builder()
                .challenge(challenges.get(0))
                .member(member)
                .build();
        memberChallengeRepository.save(memberChallenge);

        CategoryLevel categoryLevel = CategoryLevel.builder()
                .cnt(0)
                .level(1)
                .member(member)
                .category(categories.get(0))
                .isSelected(true)
                .build();
        categoryLevelRepository.save(categoryLevel);
        member.increaseSponsorProgressAndCheckIsFull(0.1);
        categoryLevel.plusCnt();

        // when
        myChallengeService.updateStatus(member.getId(), memberChallenge.getId(), false);

        // then
        CategoryLevel findCategoryLevel = categoryLevelRepository.findByMemberIdAndCategoryId(member.getId(), memberChallenge.getChallenge().getCategory().getId()).get();
        MemberChallenge findMemberChallenge = memberChallengeRepository.findById(memberChallenge.getId()).get();
        assertThat(findMemberChallenge.isDone()).isFalse();
        assertThat(member.getSponsor_progress()).isEqualTo(0);
        assertThat(findCategoryLevel.getLevel()).isEqualTo(1);
        assertThat(findCategoryLevel.getCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("getChallengesByDate")
    void getChallengesByDate() {
        // given
        Member member = createMember();
        List<List<MemberChallenge>> list = IntStream.range(0, 3)
                .mapToObj(i -> {
                    List<Challenge> challenges = createChallengesByCategories(3, categories.get(i));
                    List<MemberChallenge> memberChallenges = challenges.stream().map(c -> MemberChallenge.builder()
                            .challenge(c)
                            .member(member)
                            .build()
                    ).toList();
                    return memberChallengeRepository.saveAll(memberChallenges);
                }).toList();

        String dateFormatString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

        // when
        List<DayChallengeResponse> result = myChallengeService.getChallengesByDate(member.getId(), dateFormatString);

        // then
        assertThat(result)
                .hasSize(9)
                .first()
                .satisfies(dto -> {
                    assertThat(dto.getChallengeName()).isEqualTo("challenge1");
                    assertThat(dto.getCategoryName()).isEqualTo(categories.get(0).getName());
                    assertThat(dto.getDuration()).isEqualTo(5);
                    assertThat(dto.getLevel()).isEqualTo(1);
                    assertThat(dto.isDone()).isFalse();
                });
    }

    @Test
    @DisplayName("getChallengesCountByRange")
    void getChallengesCountByRange() {
        // given
        Member member = createMember();
        List<List<MemberChallenge>> list = IntStream.range(0, 3)
                .mapToObj(i -> {
                    List<Challenge> challenges = createChallengesByCategories(3, categories.get(i));
                    List<MemberChallenge> memberChallenges = challenges.stream().map(c -> MemberChallenge.builder()
                            .challenge(c)
                            .member(member)
                            .build()
                    ).toList();
                    return memberChallengeRepository.saveAll(memberChallenges);
                }).toList();
        LocalDate startDate = LocalDate.of(2024, 9, 5);
        LocalDate endDate = LocalDate.of(2024, 9, 8);

        // when
        List<RangeChallengeResponse> result = myChallengeService.getChallengesCountByRange(member.getId(), startDate, endDate);

        // then
        assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(rangeChallengeResponse -> {
                    assertThat(rangeChallengeResponse.getCreatedAt()).isEqualTo(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
                    assertThat(rangeChallengeResponse.getCount()).isEqualTo(9);
                });
    }

    private List<Challenge> createChallengesByCategories(int cnt, Category category) {
        List<Challenge> challenges = new ArrayList<>();
        IntStream.rangeClosed(1, cnt)
                .forEach(i -> {
                    Challenge challenge = Challenge.builder()
                            .name("challenge" + i)
                            .level(1)
                            .category(category)
                            .duration(5)
                            .build();
                    challenges.add(challenge);
                });
        return challengeRepository.saveAll(challenges);
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        IntStream.rangeClosed(1, 3)
                .forEach(i -> {
                    Category category = Category.builder()
                            .name("category" + i)
                            .build();
                    categories.add(category);
                });

        return categoryRepository.saveAll(categories);
    }

    private Member createMember() {
        Member member = Member.builder()
                .username("tester")
                .email("test@test.com")
                .provider(OAuth2Provider.GOOGLE)
                .providerId("google_foobarfoobar")
                .role(Role.ROLE_USER)
                .profileImageUrl("profileImageUrl")
                .build();
        return memberRepository.save(member);
    }
}