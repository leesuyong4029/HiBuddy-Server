package com.example.HiBuddy.domain.oauth.google;

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
public class GoogleProvider {
    public static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirectUri}")
    private String redirectUri;
    private final RestClient restClient=RestClient.create();


    public MultiValueMap<String, String> createTokenRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return params;
    }

    public String getGoogleRequestUrl() {
        List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/userinfo.email"); // 필요한 스코프

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scopes)
                .queryParam("access_type", "offline");;
        return builder.toUriString();
    }


    public GoogleTokenResponse getGoogleOAuthToken(String code) {
        Consumer<HttpHeaders> httpHeadersConsumer= httpHeaders -> {
        };

        MultiValueMap<String, String> params = createTokenRequestParams(code);

        ResponseEntity<String> response = restClient
                .method(HttpMethod.POST)
                .uri("https://oauth2.googleapis.com/token")
                .headers(httpHeadersConsumer)
                .body(params)
                .retrieve().toEntity(String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), GoogleTokenResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Google OAuth token request failed", e);
        }
    }

    public GoogleProfile getGoogleProfile(GoogleTokenResponse googleTokenResponse) {
        ResponseEntity<String> response = restClient
                .post()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(googleTokenResponse.getAccess_token()))
                .retrieve()
                .toEntity(String.class);

        try {
            return new ObjectMapper().readValue(response.getBody(), GoogleProfile.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch Kakao user profile", e);
        }
    }


}
