package com.example.gitryeokoffice.matching.domain;

import com.example.gitryeokoffice.post.domain.Post;
import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 매칭 스코어 엔티티
 * 공고와 개발자 간의 매칭 점수를 저장하여 추천 시스템에 활용
 */
@Entity
@Table(
    name = "matching_scores",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_user", columnNames = {"post_id", "user_id"})
    },
    indexes = {
        @Index(name = "idx_post_score", columnList = "post_id, score")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 추천 대상 개발자

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score; // 매칭 점수 (0.00 ~ 100.00)

    @Column(columnDefinition = "JSON")
    private String matchReasons; // 매칭 근거 배열 (JSON 형태)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime calculatedAt; // 계산 일시

    // 정적 팩토리 메서드
    public static MatchingScore create(
        Post post,
        User user,
        BigDecimal score,
        String matchReasons
    ) {
        MatchingScore matchingScore = new MatchingScore();
        matchingScore.post = post;
        matchingScore.user = user;
        matchingScore.score = score;
        matchingScore.matchReasons = matchReasons;
        return matchingScore;
    }

    // 스코어 재계산 (갱신)
    public void updateScore(BigDecimal score, String matchReasons) {
        this.score = score;
        this.matchReasons = matchReasons;
    }

    // 스코어가 특정 임계값 이상인지 확인
    public boolean isAboveThreshold(BigDecimal threshold) {
        return this.score.compareTo(threshold) >= 0;
    }
}
