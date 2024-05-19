package com.example.HiBuddy.domain.oauth.google;

import lombok.Data;

@Data
public class GoogleTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private Long expires_in;
    private String id_token;
    private String scope;
}
