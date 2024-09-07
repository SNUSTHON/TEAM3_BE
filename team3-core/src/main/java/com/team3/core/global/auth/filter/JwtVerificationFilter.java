package com.team3.core.global.auth.filter;

import com.team3.core.global.auth.application.JwtProvider;
import com.team3.core.global.auth.application.JwtService;
import com.team3.core.global.auth.model.Team3OAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.resolveAccessToken(request);
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtProvider.validate(accessToken);
        setAuthenticationToContext(accessToken);
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(String token) {
        Team3OAuth2User team3OAuth2User = jwtService.getPrincipal(token);
        OAuth2AuthenticationToken authentication =
                new OAuth2AuthenticationToken(
                        team3OAuth2User,
                        team3OAuth2User.getAuthorities(),
                        team3OAuth2User.getProvider().name()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
