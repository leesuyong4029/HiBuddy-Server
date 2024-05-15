package com.example.HiBuddy.domain.oauth.service;

import com.example.HiBuddy.domain.oauth.dto.TokenDto;
import com.example.HiBuddy.domain.oauth.google.GoogleProvider;
import com.example.HiBuddy.domain.oauth.google.GoogleService;
import com.example.HiBuddy.domain.oauth.google.GoogleTokenResponse;
import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.kakao.KakaoProvider;
import com.example.HiBuddy.domain.oauth.kakao.KakaoService;
import com.example.HiBuddy.domain.oauth.kakao.KakaoTokenResponse;
import com.example.HiBuddy.domain.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final KakaoService kakaoService;
    private final KakaoProvider kakaoProvider;
    private final GoogleService googleService;
    private final GoogleProvider googleProvider;

    @Transactional
    public TokenDto login(String providerName, String code) { //로그인 로직 모두 처리하는 메서드
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);

        TokenDto tokenDto = null;
        if (provider.getRegistrationId().equals("kakao")) {
            KakaoTokenResponse tokenResponse = kakaoService.getAccessToken(code);
            tokenDto = kakaoService.kakaoLogin(tokenResponse);
        } else if (provider.getRegistrationId().equals("google")) {
            GoogleTokenResponse tokenResponse = googleService.getAccessToken(code);
            tokenDto = googleService.googleLogin(tokenResponse);
        }

        return tokenDto;
    }

    public String getAuthCodeRequestUrl(String providerName) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        if (provider.getRegistrationId().equals("kakao")) {
            System.out.println("2");
             return kakaoProvider.getKakaoRequestUrl();
        } else if (provider.getRegistrationId().equals("google")) {
            return googleProvider.getGoogleRequestUrl();
        }
        throw new IllegalArgumentException("Unsupported provider: " + providerName);
    }

}
