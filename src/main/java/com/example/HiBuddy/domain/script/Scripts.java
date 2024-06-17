package com.example.HiBuddy.domain.script;

import com.example.HiBuddy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "script")
public class Scripts extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String scriptName;
    private Difficulty difficulty;
    private String text;
}
