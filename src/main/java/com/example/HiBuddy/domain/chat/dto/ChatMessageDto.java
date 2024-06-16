package com.example.HiBuddy.domain.chat.dto;

import com.example.HiBuddy.domain.chat.domain.MessageType;
import lombok.*;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private MessageType type;
    private String message;
    private String sender;
    private String roomId;
}

