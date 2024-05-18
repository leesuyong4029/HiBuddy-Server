package com.example.HiBuddy.domain.user;

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
