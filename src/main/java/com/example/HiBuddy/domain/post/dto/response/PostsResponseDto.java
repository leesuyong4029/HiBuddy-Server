package com.example.HiBuddy.domain.post.dto.response;

import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import lombok.*;

import java.util.List;

public class PostsResponseDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostsDto {
        private Long postId;
        private int likeNum;
       /* private int commentNum;*/
        private boolean checkLike;
        private boolean checkScrab;
        private boolean checkMine;
        private String createdAt;
        private UsersResponseDto.UsersPostDto user;
        private List<PostImageDto> postImages;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostImageDto {
        private Long imageId;
        private String imageUrl;
    }
}
