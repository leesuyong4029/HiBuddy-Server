package com.example.HiBuddy.domain.scrap;

import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.post.PostsConverter;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.scrap.response.ScrapsResponseDto;
import com.example.HiBuddy.domain.user.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.HiBuddy.domain.post.PostsConverter.toUserDto;


@Component
public class ScrapsConverter {

    public static ScrapsResponseDto.ScrapsInfoDto toScrabsInfoResultDto(Posts post, Users user, boolean checkLike, boolean checkScrap, String createdAt) {
        PostsResponseDto.UserDto userDto = toUserDto(post.getUser());

        List<PostsResponseDto.ImageDto> imageListDto = post.getPostImageList().stream()
                .map(PostsConverter::toImageDto).collect(Collectors.toList());

        return ScrapsResponseDto.ScrapsInfoDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeNum(post.getPostLikeList().size())
                .commentNum(post.getCommentsList().size())
                .checkLike(checkLike)
                .checkScrap(checkScrap)
                .isAuthor(true)
                .user(userDto)
                .postImages(imageListDto)
                .createdAt(createdAt)
                .build();
    }
    public static ScrapsResponseDto.ScrapsInfoPageDto toScrabsInfoResultPageDto(List<ScrapsResponseDto.ScrapsInfoDto> scrapsInfoDtoList, int totalPages, int totalElements,
                                                                                boolean isFirst, boolean isLast, int size, int number, int numberOfElements){
        return ScrapsResponseDto.ScrapsInfoPageDto.builder()
                .posts(scrapsInfoDtoList)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .isFirst(isFirst)
                .isLast(isLast)
                .size(size)
                .number(number)
                .numberOfElements(numberOfElements)
                .build();

    }
}
