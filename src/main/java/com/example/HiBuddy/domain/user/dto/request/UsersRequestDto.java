package com.example.HiBuddy.domain.user.dto.request;

import com.example.HiBuddy.domain.user.Country;
import lombok.*;

public class UsersRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileImageDto {
        private Long imageId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserNicknameDto {
        private String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserOnboardingDto {
        private Country country;
        private String major;
        private String nickname;
    }
}
