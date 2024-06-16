package com.example.HiBuddy.domain.chat.controller;

import com.example.HiBuddy.domain.chat.domain.ChatRoom;
import com.example.HiBuddy.domain.chat.domain.RoomType;
import com.example.HiBuddy.domain.chat.dto.ChatRoomDto;
import com.example.HiBuddy.domain.chat.service.ChatRoomService;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.user.UsersService;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/chat")
public class ChatRoomController {
    private final UsersService usersService;
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ApiResponse<ChatRoomDto> createRoom(@RequestParam String name, @RequestParam RoomType roomType, @AuthenticationPrincipal UserDetails user) {
        Long id = usersService.getUserId(user);
        ChatRoomDto chatRoomDTO = chatRoomService.createRoom(name, id, roomType);
        return ApiResponse.onSuccess(chatRoomDTO);
    }

    @DeleteMapping("/{chatRoomId}")
    public ApiResponse<SuccessStatus> deletePost(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetails user) {
        Long userId = usersService.getUserId(user);
        chatRoomService.deletePost(chatRoomId, userId);
        return ApiResponse.onSuccess(SuccessStatus.CHATROOM_DELETE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<PostsResponseDto.PostsInfoPageDto> findAllRoom(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit) {
        Page<ChatRoomDto> rooms = chatRoomService.findAllRoom(PageRequest.of(page, limit));
        PostsResponseDto.PostsInfoPageDto response = chatRoomService.convertToPostsInfoPageDto(rooms);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/room/{roomId}/participants")  // 채팅방 참여자 정보 확인하기 -> 2번째 피그마 화면 정보
    public ApiResponse<List<UsersResponseDto.UsersChatDto>> getParticipants(@PathVariable Long roomId) {
        List<UsersResponseDto.UsersChatDto> participants = chatRoomService.getParticipants(roomId);
        return ApiResponse.onSuccess(participants);
    }

    @PostMapping("/room/{roomId}/enter")
    public ApiResponse<SuccessStatus> enterRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetails user) {
        Long userId = usersService.getUserId(user);
        return chatRoomService.enterRoom(roomId, userId);
    }

    @PostMapping("/room/{roomId}/leave")
    public ApiResponse<SuccessStatus> leaveRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetails user) {
        Long userId = usersService.getUserId(user);
        return chatRoomService.leaveRoom(roomId, userId);
    }

}