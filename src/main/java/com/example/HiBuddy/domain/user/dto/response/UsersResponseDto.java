package com.example.HiBuddy.domain.user.dto.response;

import com.example.HiBuddy.domain.user.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UsersResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersMyPageDto {
        private String nickname;
        private Country country;
        private String major;
    }
}
