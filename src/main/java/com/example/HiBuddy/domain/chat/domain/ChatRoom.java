package com.example.HiBuddy.domain.chat.domain;

import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Users creator;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "chatRoom-messages")
    private Set<ChatMessage> chatMessages = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chat_room_users",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private Set<Users> participants = new HashSet<>();

    @Transient
    private static final int MAX_PARTICIPANTS = 8;

    @Transient
    private Set<WebSocketSession> sessions = new HashSet<>();

    public boolean isFull() {
        return roomType == RoomType.GROUP && participants.size() >= MAX_PARTICIPANTS;
    }

    public ChatRoom(Long id) {
        this.id = id;
    }

    public void addParticipant(Users user) {
        if (!isFull()) {
            participants.add(user);
        } else {
            throw new RuntimeException("Chat room is full");
        }
    }
}

