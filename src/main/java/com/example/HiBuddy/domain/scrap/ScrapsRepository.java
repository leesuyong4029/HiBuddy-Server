package com.example.HiBuddy.domain.scrap;

import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapsRepository extends JpaRepository<Scraps, Long> {

    Scraps findByUserAndPost(Users user, Posts post);

    boolean existsByUserAndPost(Users user, Posts post);
}
