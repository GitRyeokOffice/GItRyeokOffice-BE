package com.example.gitryeokoffice.devvibe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevVibeResultRepository extends JpaRepository<DevVibeResult, Long> {
    Optional<DevVibeResult> findByUser_Id(Long userId);
}
