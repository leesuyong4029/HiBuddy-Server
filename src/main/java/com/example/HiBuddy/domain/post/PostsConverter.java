package com.example.HiBuddy.domain.post;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.post.dto.request.PostsRequestDto;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.user.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostsConverter {

    public static PostsResponseDto.ImageDto toImageDto(Images image) {
        return PostsResponseDto.ImageDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getUrl())
                .build();
    }

    public static PostsResponseDto.UserDto toUserDto(Users user) {
        return PostsResponseDto.UserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileImage())
                .build();

    }

    public static Posts toPostsEntity(Users user, PostsRequestDto.CreatePostRequestDto postsEntityDto) {
        return Posts.builder()
                .user(user)
                .title(postsEntityDto.getTitle())
                .content(postsEntityDto.getContent())
                .postImageList(new ArrayList<>())
                .postLikeList(new ArrayList<>())
                .scrapsList(new ArrayList<>())
                .commentsList(new ArrayList<>())
                .build();
    }

    public static PostsResponseDto.PostsInfoDto toPostInfoResultDto(Posts post, Users user, boolean checkLike, boolean checkScrap, String createdAt, boolean isAuthor) {
        PostsResponseDto.UserDto userDto = toUserDto(post.getUser());

        List<PostsResponseDto.ImageDto> imageListDto = post.getPostImageList().stream()
                .map(PostsConverter::toImageDto).collect(Collectors.toList());

        return PostsResponseDto.PostsInfoDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentsList().size())
                .checkLike(checkLike)
                .checkScrap(checkScrap)
                .isAuthor(isAuthor)
                .user(userDto)
                .postImages(imageListDto)
                .createdAt(createdAt)
                .build();
    }

    public static PostsResponseDto.MainPageTopThreePostListDto mainPageTopThreePostListDto(Posts post) {
        return PostsResponseDto.MainPageTopThreePostListDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentsList().size())
                .build();
    }

    public static PostsResponseDto.PostsInfoPageDto toPostInfoResultPageDto(List<PostsResponseDto.PostsInfoDto> postsInfoDtoList, int totalPages, int totalElements,
                                                                            boolean isFirst, boolean isLast, int number, int numberOfElements) {

        return PostsResponseDto.PostsInfoPageDto.builder()
                .posts(postsInfoDtoList)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .isFirst(isFirst)
                .isLast(isLast)
                .number(number)
                .numberOfElements(numberOfElements)
                .build();

    }
}
