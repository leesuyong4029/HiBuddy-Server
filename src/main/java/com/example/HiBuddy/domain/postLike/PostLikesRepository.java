package com.example.HiBuddy.domain.postLike;

import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikesRepository  extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByUserAndPost(Users user, Posts post);
}
