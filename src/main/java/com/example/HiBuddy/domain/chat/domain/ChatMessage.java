package com.example.HiBuddy.domain.chat.domain;

import com.example.HiBuddy.domain.user.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference(value = "chatRoom-messages")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private String message;

    @Transient
    private Long roomId;

    public Long getRoomId() {
        return chatRoom != null ? chatRoom.getId() : null;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}




