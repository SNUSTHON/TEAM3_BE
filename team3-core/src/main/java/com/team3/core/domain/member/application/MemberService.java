package com.team3.core.domain.member.application;

import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.auth.model.OAuth2Provider;
import com.team3.core.global.auth.model.OAuth2ProviderUser;
import com.team3.core.global.auth.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

        return memberRepository.save(member);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("우저 정보를 찾을 수 없습니다"));
    }
}
