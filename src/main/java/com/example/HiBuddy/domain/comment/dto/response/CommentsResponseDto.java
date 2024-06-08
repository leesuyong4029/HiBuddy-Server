package com.example.HiBuddy.domain.comment.dto.response;

import com.example.HiBuddy.domain.image.Images;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommentsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private Long userId;
        private String nickname;
        private Images profileUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long commentId;
        private String comment;
        private boolean isAuthor;
        private String createdAt;
        private UserDto user;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentsInfoPageDto {
        private List<CommentDto> comments;
        private int totalPages;
        private int totalElements;
        private boolean isFirst;
        private boolean isLast;
        private int size;
        private int number;
        private int numberOfElements;
    }
}
