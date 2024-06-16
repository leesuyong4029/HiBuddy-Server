package com.example.HiBuddy.domain.chat.service;

import com.example.HiBuddy.domain.chat.domain.ChatMessage;
import com.example.HiBuddy.domain.chat.domain.ChatRoom;
import com.example.HiBuddy.domain.chat.domain.RoomType;
import com.example.HiBuddy.domain.chat.dto.ChatRoomDto;
import com.example.HiBuddy.domain.chat.repository.ChatRoomRepository;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import com.example.HiBuddy.global.redis.RedisPublisher;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import com.example.HiBuddy.global.response.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final RedisPublisher redisPublisher;
    private final ConcurrentMap<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();

    public Page<ChatRoomDto> findAllRoom(Pageable pageable) {
        Page<ChatRoom> chatRoomPage = chatRoomRepository.findAll(pageable);
        return chatRoomPage.map(this::convertToDTO);
    }

    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHATROOM_NOT_FOUND.getCode(), ErrorStatus.CHATROOM_NOT_FOUND.getMessage()));
    }

    public ChatRoomDto createRoom(String name, Long creatorId, RoomType roomType) {
        Users creator = usersRepository.findById(creatorId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage()));

        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .creator(creator)
                .roomType(roomType)
                .participants(new HashSet<>())
                .build();

        chatRoom.addParticipant(creator);
        chatRoomRepository.save(chatRoom);
        chatRooms.put(chatRoom.getId().toString(), chatRoom);

        return convertToDTO(chatRoom);
    }

    public void deletePost(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        chatRoomRepository.delete(chatRoom);
    }


    public List<UsersResponseDto.UsersChatDto> getParticipants(Long roomId) {
        ChatRoom chatRoom = findRoomById(roomId);
        return chatRoom.getParticipants().stream()
                .map(user -> UsersResponseDto.UsersChatDto.builder()
                        .userId(user.getId())
                        .nickname(user.getNickname())
                        .country(user.getCountry())
                        .major(user.getMajor())
                        .profileImage(user.getProfileImage())
                        .build())
                .collect(Collectors.toList());
    }

    public ApiResponse<SuccessStatus> enterRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHATROOM_NOT_FOUND.getCode(), ErrorStatus.CHATROOM_NOT_FOUND.getMessage()));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage()));

        chatRoom.addParticipant(user);
        chatRoomRepository.save(chatRoom);

        // 입장 메시지를 Redis로 발행
        ChatMessage enterMessage = new ChatMessage();
        enterMessage.setSenderId(userId);
        enterMessage.setMessage(user.getNickname() + "님이 입장하셨습니다.");
        redisPublisher.publish(ChannelTopic.of("chatroom"), enterMessage);

        return ApiResponse.onSuccess(SuccessStatus.CHATROOM_ENTER_SUCCESS);
    }

    public ApiResponse<SuccessStatus> leaveRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHATROOM_NOT_FOUND.getCode(), ErrorStatus.CHATROOM_NOT_FOUND.getMessage()));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage()));

        chatRoom.getParticipants().remove(user);
        chatRoomRepository.save(chatRoom);

        // 퇴장 메시지를 Redis로 발행
        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setSenderId(userId);
        leaveMessage.setMessage(user.getNickname() + "님이 퇴장하셨습니다.");
        redisPublisher.publish(ChannelTopic.of("chatroom"), leaveMessage);

        return ApiResponse.onSuccess(SuccessStatus.CHATROOM_LEAVE_SUCCESS);
    }

    private ChatRoomDto convertToDTO(ChatRoom chatRoom) {
        Set<UsersResponseDto.UsersPostDto> participantDTOs = chatRoom.getParticipants().stream()
                .map(user -> UsersResponseDto.UsersPostDto.builder()
                        .userId(user.getId())
                        .nickname(user.getNickname())
                        .country(user.getCountry())
                        .major(user.getMajor())
                        .build())
                .collect(Collectors.toSet());

        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .roomType(chatRoom.getRoomType())
                .creatorUserId(chatRoom.getCreator().getId())
                .participants(participantDTOs)
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }

    public PostsResponseDto.PostsInfoPageDto convertToPostsInfoPageDto(Page<ChatRoomDto> page) {
        List<PostsResponseDto.PostsInfoDto> posts = page.getContent().stream().map(this::convertToPostsInfoDto).collect(Collectors.toList());
        return PostsResponseDto.PostsInfoPageDto.builder()
                .posts(posts)
                .totalPages(page.getTotalPages())
                .totalElements((int) page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .number(page.getNumber())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }

    private PostsResponseDto.PostsInfoDto convertToPostsInfoDto(ChatRoomDto chatRoomDTO) {
        return PostsResponseDto.PostsInfoDto.builder()
                .postId(chatRoomDTO.getId())
                .title(chatRoomDTO.getName())
                .createdAt(chatRoomDTO.getCreatedAt().toString())
                .user(PostsResponseDto.UserDto.builder()
                        .userId(chatRoomDTO.getCreatorUserId())
                        .build())
                .build();
    }
}
