package com.team3.core.global.auth.handler;

import com.team3.core.global.auth.model.Team3OAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Team3OAuth2User principal = (Team3OAuth2User) authentication.getPrincipal();
        System.out.println("principal.getName() = " + principal.getName());
//        TokenDto tokenDto = jwtProvider.createJwt(principal);
//        String targetUri = createUri(tokenDto, principal.getId());
//        jwtService.saveRefreshToken(principal, tokenDto);
//        jwtProvider.setHeader(response, tokenDto);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        DataResponse<TokenDto> dto = DataResponse.of("로그인 성공", tokenDto);
//        objectMapper.writeValue(response.getWriter(), dto);
    }
}
