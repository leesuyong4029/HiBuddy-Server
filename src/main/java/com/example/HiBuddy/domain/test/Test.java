package com.example.HiBuddy.domain.test;

import com.example.HiBuddy.domain.script.Scripts;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_history")
public class Test extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String scriptName;
    private LocalDateTime testDate;
    private String difficulty;
    private String recognizedText;
    private double pitch;
    private double basePitch;
    private String pitchLevel;
    private int pronunciationScore;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    private Scripts script;  // 추가된 부분

}
