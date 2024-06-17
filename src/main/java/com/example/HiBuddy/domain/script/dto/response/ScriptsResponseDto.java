package com.example.HiBuddy.domain.script.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScriptsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptDto {
        private Long scriptId;
        private String scriptName;
        private String difficulty;
        private String text;

    }
}
