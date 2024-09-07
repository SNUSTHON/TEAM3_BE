package com.team3.core.domain.member.application;

import com.team3.core.domain.category.dao.CategoryLevelRepository;
import com.team3.core.domain.category.dao.CategoryRepository;
import com.team3.core.domain.category.domain.Category;
import com.team3.core.domain.category.domain.CategoryLevel;
import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.auth.model.OAuth2Provider;
import com.team3.core.global.auth.model.OAuth2ProviderUser;
import com.team3.core.global.auth.model.Role;
import com.team3.core.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.team3.core.global.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryLevelRepository categoryLevelRepository;

    public Member createOrUpdate(OAuth2ProviderUser oAuth2ProviderUser) {
        return memberRepository.findByEmail(oAuth2ProviderUser.getEmail())
                .orElseGet(() -> this.registerByOAuth2(oAuth2ProviderUser));
    }

    private Member registerByOAuth2(OAuth2ProviderUser oAuth2ProviderUser) {
        Role role = oAuth2ProviderUser.getEmail().equals("maruhan1016@gmail.com")
                ? Role.ROLE_ADMIN
                : Role.ROLE_USER;

        Member member = Member.builder()
                .username(oAuth2ProviderUser.getUsername())
                .email(oAuth2ProviderUser.getEmail())
                .provider(OAuth2Provider.valueOf(oAuth2ProviderUser.getProvider().toUpperCase()))
                .providerId(oAuth2ProviderUser.getProviderId())
                .profileImageUrl(oAuth2ProviderUser.getProfileImageUrl())
                .role(role)
                .build();


        Member savedMember = memberRepository.save(member);

        List<Category> categories = categoryRepository.findAll();

        Random random = new Random();

        Set<Integer> uniqueIntegers = new HashSet<>();

        // 중복되지 않는 랜덤한 정수 3개 생성
        while (uniqueIntegers.size() < 3) {
            int randomInt = random.nextInt(1, categories.size() + 1);
            uniqueIntegers.add(randomInt);
        }

        for (int i = 0; i < categories.size(); i++) {
            boolean isSelected = uniqueIntegers.contains(i + 1);

            CategoryLevel categoryLevel = CategoryLevel.builder()
                    .member(savedMember)
                    .category(categories.get(i))
                    .level(1)
                    .isSelected(isSelected)
                    .cnt(0)
                    .build();

            categoryLevelRepository.save(categoryLevel);
        }

        return savedMember;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
