package com.example.gitryeokoffice.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공고별 선호 성향 엔티티
 * 팀장이 원하는 팀원의 Dev-Vibe 성향 설정
 */
@Entity
@Table(
    name = "post_target_vibes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_axis", columnNames = {"post_id", "axis"})
    },
    indexes = {
        @Index(name = "idx_post_id", columnList = "post_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTargetVibe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 공고

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VibeAxis axis; // 성향 축 (WORK_STYLE, ACTIVITY_PATTERN, TIME_OF_DAY)

    @Column(nullable = false, length = 50)
    private String desiredLabel; // 선호 라벨 (예: PLANNED, IMPROVISATION, STEADY, FOCUS, MORNING, NIGHT)

    // 정적 팩토리 메서드
    public static PostTargetVibe create(Post post, VibeAxis axis, String desiredLabel) {
        PostTargetVibe targetVibe = new PostTargetVibe();
        targetVibe.post = post;
        targetVibe.axis = axis;
        targetVibe.desiredLabel = desiredLabel;
        return targetVibe;
    }

    // 선호 라벨 수정
    public void updateDesiredLabel(String desiredLabel) {
        this.desiredLabel = desiredLabel;
    }
}
