package com.example.HiBuddy.domain.post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    Page<Posts> findByUserId(Long userId, Pageable pageable);

    Optional<Posts> findByUserIdAndId(Long userId, Long postId);

    @Query("SELECT p FROM Posts p LEFT JOIN p.postLikeList l GROUP BY p ORDER BY COUNT(l) DESC")
    List<Posts> findTopThreePosts(Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:keyword%")
    Page<Posts> findByTitle(@Param("keyword") String keyword, Pageable pageable);
}
