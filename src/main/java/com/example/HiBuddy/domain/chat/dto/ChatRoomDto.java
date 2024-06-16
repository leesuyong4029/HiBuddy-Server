package com.example.HiBuddy.domain.chat.dto;

import com.example.HiBuddy.domain.chat.domain.ChatRoom;
import com.example.HiBuddy.domain.chat.domain.RoomType;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class ChatRoomDto {
    private Long id;
    private String name;
    private RoomType roomType;
    private Long creatorUserId;
    private Set<UsersResponseDto.UsersPostDto> participants;
    private LocalDateTime createdAt;
}