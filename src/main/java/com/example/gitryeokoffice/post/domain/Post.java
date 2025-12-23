package com.example.gitryeokoffice.post.domain;

import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 팀빌딩 공고 엔티티
 */
@Entity
@Table(
    name = "posts",
    indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_deadline", columnList = "deadline"),
        @Index(name = "idx_owner_user_id", columnList = "owner_user_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner; // 공고 작성자 (팀장)

    @Column(nullable = false, length = 200)
    private String title; // 공고 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 공고 본문

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalType goalType; // 프로젝트 목표

    @Column
    private Integer expectedDurationWeeks; // 예상 프로젝트 기간 (주 단위)

    @Column
    private LocalDate deadline; // 모집 마감일

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0; // 조회수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.OPEN; // 공고 상태

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 정적 팩토리 메서드
    public static Post create(
        User owner,
        String title,
        String description,
        GoalType goalType,
        Integer expectedDurationWeeks,
        LocalDate deadline
    ) {
        Post post = new Post();
        post.owner = owner;
        post.title = title;
        post.description = description;
        post.goalType = goalType;
        post.expectedDurationWeeks = expectedDurationWeeks;
        post.deadline = deadline;
        post.viewCount = 0;
        post.status = PostStatus.OPEN;
        return post;
    }

    // 공고 수정
    public void update(
        String title,
        String description,
        GoalType goalType,
        Integer expectedDurationWeeks,
        LocalDate deadline
    ) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (goalType != null) this.goalType = goalType;
        if (expectedDurationWeeks != null) this.expectedDurationWeeks = expectedDurationWeeks;
        if (deadline != null) this.deadline = deadline;
    }

    // 조회수 증가
    public void incrementViewCount() {
        this.viewCount++;
    }

    // 공고 마감
    public void close() {
        this.status = PostStatus.CLOSED;
    }

    // 공고 재오픈
    public void reopen() {
        this.status = PostStatus.OPEN;
    }

    // 본인 작성 공고인지 확인
    public boolean isOwnedBy(User user) {
        return this.owner.getId().equals(user.getId());
    }
}
