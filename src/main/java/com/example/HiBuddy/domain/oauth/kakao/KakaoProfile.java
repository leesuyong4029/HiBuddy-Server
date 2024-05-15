package com.example.HiBuddy.domain.oauth.kakao;

import lombok.Data;

@Data
public class KakaoProfile {
    public Long id;
    public String connected_at;
    public KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
    }
}
