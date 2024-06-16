package com.example.HiBuddy.global.redis;

import com.example.HiBuddy.domain.chat.domain.ChatMessage;
import com.example.HiBuddy.domain.chat.domain.ChatRoom;
import com.example.HiBuddy.domain.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = new StringRedisSerializer().deserialize(message.getBody());
            log.info("Received raw message from Redis: {}", publishMessage);

            if (publishMessage != null && !publishMessage.trim().isEmpty()) {
                ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
                log.info("Deserialized ChatMessageDto: {}", chatMessageDto);

                ChatMessage chatMessage = ChatMessage.builder()
                        .type(chatMessageDto.getType())
                        .message(chatMessageDto.getMessage())
                        .senderId(Long.parseLong(chatMessageDto.getSender()))
                        .chatRoom(new ChatRoom(Long.parseLong(chatMessageDto.getRoomId())))
                        .build();

                log.info("Built ChatMessage: {}", chatMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
                log.info("Message sent to WebSocket: /sub/chat/room/{}", chatMessage.getRoomId());
            } else {
                log.warn("Received empty or null message from Redis");
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
