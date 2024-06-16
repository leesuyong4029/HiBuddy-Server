package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.domain.chat.domain.ChatMessage;
import com.example.HiBuddy.domain.chat.domain.ChatRoom;
import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private String username;

    @Enumerated(EnumType.STRING)
    @Column
    private Country country;

    @Column
    private Major major;

    @Column
    private Status status;

    @Column
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id")
    private Images profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> images;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ChatMessage> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<ChatRoom> createdChatRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private List<ChatRoom> participatedChatRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @Transient
    private static final Map<Long, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
