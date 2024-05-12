package com.example.HiBuddy.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersById(Long userId);
    Optional<Users> findByEmail(String email);
    Optional<Users>findByUsername(String username);
}
