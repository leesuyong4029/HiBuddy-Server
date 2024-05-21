package com.example.HiBuddy.domain.user.dto.response;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.user.Country;
import lombok.*;

public class UsersResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersMyPageDto {
        private String nickname;
        private Country country;
        private String major;
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
        private String major;
        private String nickname;
    }
}
