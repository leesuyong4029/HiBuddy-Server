package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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
@Getter
@Setter
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    @Column
    private String username;

    @Column // enum으로 조져도 됨
    private String country;

    @Column
    private String major;

    @Column
    private boolean status;

    @Column
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    public Users update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Users(String email, String oauth){
        this.email = email;
    }

}
