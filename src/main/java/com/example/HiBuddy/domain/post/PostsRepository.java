package com.example.HiBuddy.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByUserId(Long userId, Pageable pageable);
}
