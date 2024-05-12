package com.example.HiBuddy.domain.oauth.dto;

import java.util.Collections;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes; // 원래 데이터의 전체 구조를 저장
    private Map<String, Object> kakaoAccount; // kakao_account 맵을 저장

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        if (attributes.containsKey("kakao_account")) {
            this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        } else {
            this.kakaoAccount = Collections.emptyMap(); // kakao_account 키가 없는 경우 안전하게 처리
        }
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        if (attributes.containsKey("id")) {
            return attributes.get("id").toString();
        }
        return null; // "id" 키가 없는 경우 null 반환
    }

    @Override
    public String getEmail() {
        if (kakaoAccount.containsKey("email")) {
            return kakaoAccount.get("email").toString();
        }
        return null; // "email" 키가 없는 경우 null 반환
    }
}


