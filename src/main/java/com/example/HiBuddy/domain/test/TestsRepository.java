package com.example.HiBuddy.domain.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestsRepository extends JpaRepository<Test, Long> {
    Page<Test> findAll(Long userId, Pageable pageable);
}
