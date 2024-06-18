package com.example.HiBuddy.domain.test.dto.response;

import lombok.*;

import java.util.List;

public class TestsResponseDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestStartResultDto {
        private Long testId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResultDto {
        private Long testId;
        private Long scriptId;
        private String scriptName;
        private String testDate;
        private String difficulty;
        private String recognizedText;
        private double pitch;
        private double basePitch;
        private String pitchLevel;
        private double pronunciationScore;
        private UserResponseDto user;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestHistoryDto {
        private Long testId;
        private Long scriptId;
        private String scriptName;
        private String testDate;
        private String difficulty;
        private String recognizedText;
        private double pitch;
        private double basePitch;
        private String pitchLevel;
        private double pronunciationScore;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDto {
        private Long testId;
        private Long scriptId;
        private String scriptName;
        private String testDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestHistoryMainPageListDto {
        private List<TestDto> test;
        private int totalPages;
        private int totalElements;
        private boolean isFirst;
        private boolean isLast;
        private int number;
        private int numberOfElements;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponseDto {
        private Long userId;
        private String username;
        private String nickname;
    }
}
