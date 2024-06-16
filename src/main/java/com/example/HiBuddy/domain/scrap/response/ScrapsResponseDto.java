package com.example.HiBuddy.domain.scrap.response;

import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import lombok.*;

import java.util.List;

public class ScrapsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapsInfoDto {
        private Long postId;
        private String title;
        private String content;
        private String createdAt;
        private Integer likeNum;
        private Integer commentNum;
        private boolean isAuthor;
        private boolean checkLike;
        private boolean checkScrap;
        private PostsResponseDto.UserDto user;
        private List<PostsResponseDto.ImageDto> postImages;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapsInfoPageDto {
        private List<ScrapsInfoDto> posts;
        private int totalPages;
        private int totalElements;
        private boolean isFirst;
        private boolean isLast;
        private int number;
        private int numberOfElements;
    }
}
