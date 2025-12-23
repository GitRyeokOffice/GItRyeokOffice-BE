package com.example.gitryeokoffice.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 대한 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDisplayName(String displayName);

    Optional<User> findByGithubLogin(String githubLogin);

    Optional<User> findByEmail(String email);

    boolean existsByDisplayName(String displayName);

    boolean existsByGithubLogin(String githubLogin);

    boolean existsByEmail(String email);
}
