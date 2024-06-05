package com.example.HiBuddy.domain.comment.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentRequestDto {

        @Size(max = 500, message = "댓글은 최대 1,000자까지 입력 가능합니다.")
        private String comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentRequestDto {

        @Size(max = 500, message = "댓글은 최대 1,000자까지 입력 가능합니다.")
        private String comment;
    }
}
