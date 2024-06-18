package com.example.HiBuddy.domain.test;

import com.example.HiBuddy.domain.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TestsRepository extends JpaRepository<Test, Long> {
    Page<Test> findByUser(Users user, Pageable pageable);
    Optional<Test> findByIdAndUser(Long id, Users user);
    void deleteAllByUser(Users user);
}
