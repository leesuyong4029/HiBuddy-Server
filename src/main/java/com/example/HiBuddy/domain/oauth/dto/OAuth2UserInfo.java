package com.example.HiBuddy.domain.oauth.dto;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
}
