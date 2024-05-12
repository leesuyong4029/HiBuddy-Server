package com.example.HiBuddy.domain.user;

import lombok.*;

public class UsersRequestDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersOAuthDto {
        private String email; // email
        private String username; // custom username
    }

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
    public static class UserIdDto {
        private Long id;
    }
}
