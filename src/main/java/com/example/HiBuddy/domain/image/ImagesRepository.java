package com.example.HiBuddy.domain.image;

import com.example.HiBuddy.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUrl(String url);
    void deleteAllByUser(Users user);
}
