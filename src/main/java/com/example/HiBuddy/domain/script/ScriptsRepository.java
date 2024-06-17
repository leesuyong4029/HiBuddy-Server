package com.example.HiBuddy.domain.script;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptsRepository extends JpaRepository<Scripts, Long> {
}
