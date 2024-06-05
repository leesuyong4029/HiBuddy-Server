package com.example.HiBuddy.domain.post;

import com.example.HiBuddy.domain.comment.Comments;
import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.postLike.PostLikes;
import com.example.HiBuddy.domain.scrap.Scraps;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.entity.BaseEntity;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.PostLikesHandler;
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

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 500, columnDefinition = "TEXT", nullable = false)
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
    private List<Scraps> scrapsList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comments> commentsList = new ArrayList<>();

    @Column(name = "like_num")
    private Integer likeNum;

    public void incrementLikeNum() {
        this.likeNum++;
    }

    public void decrementLikeNum() {
        if (this.likeNum > 0) {
            this.likeNum--;
        } else {
            throw new PostLikesHandler(ErrorStatus.POSTLIKE_NOT_FOUND);
        }
    }
}
