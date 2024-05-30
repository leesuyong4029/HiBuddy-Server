package com.example.HiBuddy.domain.post;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.postLike.PostLikes;
import com.example.HiBuddy.domain.scrab.Scrabs;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Posts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content", length = 500, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "boolean default false")
    private Boolean isAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Images> postImageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLikes> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Scrabs> scrabList = new ArrayList<>();
}
