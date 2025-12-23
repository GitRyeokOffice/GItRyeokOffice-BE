package com.example.gitryeokoffice.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * GitHub 활동 로그 기반 AI 성향 분석을 위한 사용자 정보 관리
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_github_login", columnList = "githubLogin"),
    @Index(name = "idx_role_type", columnList = "roleType"),
    @Index(name = "idx_created_at", columnList = "createdAt")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String githubLogin; // GitHub 사용자명 (분석 데이터 수집 키)

    @Column(unique = true)
    private String email; // 로그인/알림용

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(length = 100)
    private String displayName; // 서비스 내 표시 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType; // DEVELOPER, NON_DEVELOPER

    @Column(length = 100)
    private String organization; // 소속 (학교/회사)

    @Enumerated(EnumType.STRING)
    private Position position; // FE, BE, AI, MOBILE, DESIGN, PM, ETC

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer projectExperienceCount = 0; // 프로젝트 경험 횟수

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSprout = false; // 새싹 개발자 여부

    @Column(length = 500)
    private String profileImageUrl; // GitHub 프로필 이미지 URL

    @Column(columnDefinition = "TEXT")
    private String bio; // 자기소개

    @Column(length = 255)
    private String githubUrl; // GitHub 프로필 링크

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 정적 팩토리 메서드
    public static User create(
        String githubLogin,
        String email,
        String password,
        String displayName,
        RoleType roleType,
        Position position,
        String organization,
        Integer projectExperienceCount,
        Boolean isSprout
    ) {
        User user = new User();
        user.githubLogin = githubLogin;
        user.email = email;
        user.password = password;
        user.displayName = displayName;
        user.roleType = roleType;
        user.position = position;
        user.organization = organization;
        user.projectExperienceCount = projectExperienceCount != null ? projectExperienceCount : 0;
        user.isSprout = isSprout != null ? isSprout : false;
        return user;
    }

    // GitHub 프로필 정보 업데이트
    public void updateGithubProfile(String profileImageUrl, String githubUrl, String bio) {
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.bio = bio;
    }

    // 사용자 정보 수정
    public void updateProfile(
        String displayName,
        Position position,
        String organization,
        Integer projectExperienceCount
    ) {
        if (displayName != null) this.displayName = displayName;
        if (position != null) this.position = position;
        if (organization != null) this.organization = organization;
        if (projectExperienceCount != null) this.projectExperienceCount = projectExperienceCount;
    }

    // 새싹 상태 토글
    public void toggleSproutStatus() {
        this.isSprout = !this.isSprout;
    }
}
