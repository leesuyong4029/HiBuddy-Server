package com.example.HiBuddy.global.security;

import com.example.HiBuddy.domain.oauth.jwt.JwtFilter;
import com.example.HiBuddy.domain.oauth.jwt.JwtLogoutFilter;
import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import com.example.HiBuddy.domain.oauth.service.CustomOAuth2UserService;
import com.example.HiBuddy.global.response.exception.handler.UsersFailureHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@Configurable
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig { // Servlet Container의 SecurityConfig 생성\
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UsersSuccessHandler usersSuccessHandler;
    private final UsersFailureHandler usersFailureHandler;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 인가 작업 진행
        http
                .csrf(CsrfConfigurer::disable) // csrf 비활성화
                .formLogin(FormLoginConfigurer::disable) // formLogin 비활성화
                .httpBasic(HttpBasicConfigurer::disable) // httpBasic 비활성화
                .sessionManagement(sessionManagement-> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 방식 x, oauth jwt STATELESS
                )
                .oauth2Login(oauth2->oauth2  // oauth2 login
                        .redirectionEndpoint(endpoint->endpoint.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(usersSuccessHandler)
                        .failureHandler(usersFailureHandler)
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/hibuddy/v1/auth/**","/oauth2/authorization/**","/reissue").permitAll()
                        .requestMatchers("/hibuddy/v1/auth/user/**").authenticated() // 유저 페이지는 권한이 있어야 댐
                        .anyRequest().permitAll());
        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new JwtLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);
        return http.build();
    }
}

