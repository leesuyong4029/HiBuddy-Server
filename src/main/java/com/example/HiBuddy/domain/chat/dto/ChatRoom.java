package com.example.HiBuddy.domain.chat.dto;

import com.example.HiBuddy.domain.chat.service.ChatService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
public class ChatRoom {
    private String roomId;
    private String name;

    @JsonIgnore
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
            log.info("User {} entered room {}", chatMessage.getSender(), roomId);
        } else if (chatMessage.getType().equals(ChatMessage.MessageType.TALK)) {
            log.info("User {} sent message in room {}: {}", chatMessage.getSender(), roomId, chatMessage.getMessage());
        }
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
        log.info("Session removed: {}", session.getId());
    }
}
