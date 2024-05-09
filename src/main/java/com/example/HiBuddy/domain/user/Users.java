package com.example.HiBuddy.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    @Column // enum으로 조져도 됨
    private String country;

    @Column
    private String major;

    @Column
    private boolean status;

    @Column
    private String nickname;

    @CreationTimestamp
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    @Column(unique = true, nullable = false)
    private String email;

    private String oauth;

    @Column
    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override // 계정 만료 여부
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않음
    }

    @Override // 계중 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true; // true -> 잠금되지 않음
    }

    @Override // 패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정이 사용 가능한지 확인하는 로직
    public boolean isEnabled() {
        return true; // true -> 사용 가능
    }


    public Users update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    public Users(String email, String oauth){
        this.email = email;
        this.oauth = oauth;
    }

    public void updateRefreshToken(String refreshToken){
        System.out.println("helo");
        this.refreshToken = refreshToken;
    }
}
