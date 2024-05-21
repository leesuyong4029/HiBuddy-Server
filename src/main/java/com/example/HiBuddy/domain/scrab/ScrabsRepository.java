package com.example.HiBuddy.domain.scrab;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrabsRepository extends JpaRepository<Scrabs, Long> {
    Page<Scrabs> findByUserId(Long userId, Pageable pageable);
}
