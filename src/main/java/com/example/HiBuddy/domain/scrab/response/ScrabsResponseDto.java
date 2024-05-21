package com.example.HiBuddy.domain.scrab.response;

import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import lombok.*;

import java.util.List;

public class ScrabsResponseDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrabsDto {
        private Long postId;
        private int likeNum;
        private boolean checkLike;
        private boolean checkScrab;
        private boolean checkMine;
        private String createdAt;
        private UsersResponseDto.UsersPostDto user;
        private List<PostsResponseDto.PostImageDto> postImages;
    }
}
