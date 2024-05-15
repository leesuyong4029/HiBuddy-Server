package com.example.HiBuddy.domain.oauth.kakao;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class KakaoProvider {
    public static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirectUri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.tokenUri}")
    private String tokenRequestUri;

    @Value("${spring.security.oauth2.client.provider.kakao.userInfoUri}")
    private String userInfoUri;
    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";
    private final RestClient restClient=RestClient.create();

    public MultiValueMap<String, String> createTokenRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return params;
    }

    public String getKakaoRequestUrl() {
        List<String> scopes = Arrays.asList("account_email"); // 필요한 스코프

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri);                ;
        return builder.toUriString();
    }


    public KakaoTokenResponse getKakaoOAuthToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        Consumer<HttpHeaders>httpHeadersConsumer=httpHeaders -> {
            headers.add("Content-type", contentType);
        };

        MultiValueMap<String, String> params = createTokenRequestParams(code);

        ResponseEntity<String> response = restClient
                .method(HttpMethod.POST)
                .uri(tokenRequestUri)
                .headers(httpHeadersConsumer)
                .body(params)
                .retrieve().toEntity(String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), KakaoTokenResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Kakao OAuth token request failed", e);
        }
    }

    public KakaoProfile getKakaoProfile(KakaoTokenResponse kakaoTokenResponse) {
        ResponseEntity<String> response = restClient
                .post()
                .uri(userInfoUri)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(kakaoTokenResponse.getAccess_token()))
                .retrieve()
                .toEntity(String.class);

        try {
            return new ObjectMapper().readValue(response.getBody(), KakaoProfile.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch Kakao user profile", e);
        }
    }

}
