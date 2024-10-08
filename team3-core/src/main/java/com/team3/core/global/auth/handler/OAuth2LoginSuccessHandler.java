package com.team3.core.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.core.global.auth.application.JwtProvider;
import com.team3.core.global.auth.application.JwtService;
import com.team3.core.global.auth.dto.TokenDto;
import com.team3.core.global.auth.model.Team3OAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Team3OAuth2User principal = (Team3OAuth2User) authentication.getPrincipal();
        TokenDto tokenDto = jwtProvider.createJwt(principal);
//        String targetUri = createUri(tokenDto, principal.getId());
        jwtProvider.setHeader(response, tokenDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
//        DataResponse<TokenDto> dto = DataResponse.of("로그인 성공", tokenDto);
        objectMapper.writeValue(response.getWriter(), tokenDto);
    }
}
