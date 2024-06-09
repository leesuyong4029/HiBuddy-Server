package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.domain.chat.dto.ChatMessage;
import com.example.HiBuddy.domain.chat.dto.ChatRoom;
import com.example.HiBuddy.domain.chat.service.ChatService;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = chatService.getObjectMapper().readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        if (room != null) {
            room.handleActions(session, chatMessage, chatService);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Session connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatService.getChatRooms().values().forEach(room -> room.removeSession(session));
        log.info("Session disconnected: {}", session.getId());
    }
}
