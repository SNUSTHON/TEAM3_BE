package com.team3.core.global.auth.application;

import com.team3.core.domain.member.application.MemberService;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.auth.model.Team3OAuth2User;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public Team3OAuth2User getPrincipal(String accessToken) {
        Claims payload = jwtProvider.getPayload(accessToken);
        String email = payload.getSubject();
        Member findMember = memberService.findMemberByEmail(email);
        return Team3OAuth2User.of(findMember);
    }
}
