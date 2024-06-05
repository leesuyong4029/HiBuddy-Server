package com.example.HiBuddy.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private Long userId;
        private String nickname;
        private String profileUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ImageDto {
        private Long imageId;
        private String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostsInfoDto {
        private Long postId;
        private String title;
        private String content;
        private String createdAt;
        private Integer likeNum;
        private Integer commentNum;
        private boolean isAuthor;
        private boolean checkLike;
        private boolean checkScrap;
        private UserDto user;
        private List<ImageDto> postImages;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageTopThreePostListDto {
        private Long postId;
        private String title;
        private Integer likeNum;
        private Integer commentNum;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostsInfoPageDto {
        private List<PostsInfoDto> result;
        private int totalPages;
        private int totalElements;
        private boolean isFirst;
        private boolean isLast;
        private int size;
        private int number;
        private int numberOfElements;

    }

}
