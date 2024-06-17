package com.example.HiBuddy.domain.user.dto.response;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.user.Country;
import com.example.HiBuddy.domain.user.Major;
import lombok.*;

public class UsersResponseDto {

    @Data
    @Builder
    public static class UsersMyPageDto {
        private String nickname;
        private Country country;
        private Major major;
        private String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersChatDto {
        private Long userId;
        private String nickname;
        private Country country;
        private Major major;
        private Images profileImage;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersPostDto {
        private Long userId;
        private Country country;
        private Major major;
        private String nickname;
    }
}
