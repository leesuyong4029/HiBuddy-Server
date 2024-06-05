package com.example.HiBuddy.domain.post.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class PostsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePostRequestDto {

        @Size(max = 100, message = "게시글의 제목은 최대 100자까지 입력 가능합니다.")
        private String title;
        @Size(max = 500, message = "게시글의 본문은 최대 500자까지 입력 가능합니다.")
        private String content;
        private List<Long> imageIds = new ArrayList<>();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostRequestDto {

        @Size(max = 100, message = "게시글의 제목은 최대 100자까지 입력 가능합니다.")
        private String title;
        @Size(max = 500, message = "게시글의 본문은 최대 500자까지 입력 가능합니다.")
        private String content;
        private List<Long> imageIds = new ArrayList<>();
    }
}
