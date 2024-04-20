package com.example.HiBuddy.domain.oauth.kakao;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KakaoProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String TOKEN_TYPE = "Bearer ";

    @Value("${security.oauth2.client.registration.kakao.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.registration.kakao.redirectUri}")
    private String redirectUri;

    @Value("${security.oauth2.client.registration.provider.kakao.tokenUri}")
    private String tokenRequestUri;

    @Value("${security.oauth2.client.registration.provider.kakao.userInfoUri}")
    private String userInfoUri;

    @Value("${security.oauth2.client.registration.provider.kakao.authorizationUri}")
    private String authorizationUri;

    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    private final RestTemplate restTemplate;

    public KakaoTokenResponse getKakaoOAuthToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", contentType);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                tokenRequestUri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        try {
            return new ObjectMapper().readValue(response.getBody(), KakaoTokenResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Kakao OAuth token request failed", e);
        }
    }

    public KakaoProfile getKakaoProfile(KakaoTokenResponse kakaoTokenResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoTokenResponse.getAccess_token());
        headers.add("Content-type", contentType);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.POST,
                request,
                String.class
        );

        try {
            return new ObjectMapper().readValue(response.getBody(), KakaoProfile.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch Kakao user profile", e);
        }
    }

}
