package com.team3.core.global.config;

import com.team3.core.global.auth.application.Team3OAuth2UserService;
import com.team3.core.global.auth.filter.JwtExceptionFilter;
import com.team3.core.global.auth.filter.JwtVerificationFilter;
import com.team3.core.global.auth.handler.JwtAccessDeniedHandler;
import com.team3.core.global.auth.handler.OAuth2EntryPoint;
import com.team3.core.global.auth.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Team3OAuth2UserService team3OAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2EntryPoint oAuth2EntryPoint;
    private final JwtVerificationFilter jwtVerificationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring()
                        .requestMatchers("/static/favicon.ico")
                        .requestMatchers(PathRequest.toH2Console())
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .rememberMe(RememberMeConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> {
                    authz.requestMatchers("/").permitAll();
                    authz.requestMatchers("/swagger-ui/index.html").permitAll();
                    authz.requestMatchers(PathRequest.toH2Console()).permitAll();
                    authz.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig
                                                .userService(team3OAuth2UserService)
                                )
                                .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(jwtVerificationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtVerificationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(oAuth2EntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler));

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
