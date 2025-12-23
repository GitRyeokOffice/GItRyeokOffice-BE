package com.example.gitryeokoffice.application.domain;

import com.example.gitryeokoffice.post.domain.Post;
import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 지원 엔티티
 * 사용자가 공고에 지원하는 행위
 */
@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_applicant", columnNames = {"post_id", "applicant_user_id"})
    },
    indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_applicant_user_id", columnList = "applicant_user_id"),
        @Index(name = "idx_created_at", columnList = "createdAt")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 지원한 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_user_id", nullable = false)
    private User applicant; // 지원자

    @Column(columnDefinition = "TEXT")
    private String message; // 지원 메시지

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED; // 지원 상태

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 지원일시

    // 정적 팩토리 메서드
    public static Application create(Post post, User applicant, String message) {
        Application application = new Application();
        application.post = post;
        application.applicant = applicant;
        application.message = message;
        application.status = ApplicationStatus.APPLIED;
        return application;
    }

    // 지원 승인
    public void accept() {
        this.status = ApplicationStatus.ACCEPTED;
    }

    // 지원 거절
    public void reject() {
        this.status = ApplicationStatus.REJECTED;
    }

    // 본인 지원인지 확인
    public boolean isAppliedBy(User user) {
        return this.applicant.getId().equals(user.getId());
    }

    // 공고 소유자인지 확인 (승인/거절 권한)
    public boolean canManageBy(User user) {
        return this.post.isOwnedBy(user);
    }
}
