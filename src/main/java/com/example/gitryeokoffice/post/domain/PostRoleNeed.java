package com.example.gitryeokoffice.post.domain;

import com.example.gitryeokoffice.user.domain.Position;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 공고별 모집 포지션 엔티티
 */
@Entity
@Table(
    name = "post_role_needs",
    indexes = {
        @Index(name = "idx_post_id", columnList = "post_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRoleNeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 공고

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position; // 포지션 (FE, BE, AI, MOBILE, DESIGN, PM, ETC)

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer headcount = 1; // 모집 인원 수

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSproutWelcome = false; // 새싹 개발자 참여 가능 여부

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드
    public static PostRoleNeed create(
        Post post,
        Position position,
        Integer headcount,
        Boolean isSproutWelcome
    ) {
        PostRoleNeed roleNeed = new PostRoleNeed();
        roleNeed.post = post;
        roleNeed.position = position;
        roleNeed.headcount = headcount != null ? headcount : 1;
        roleNeed.isSproutWelcome = isSproutWelcome != null ? isSproutWelcome : false;
        return roleNeed;
    }

    // 모집 정보 수정
    public void update(Integer headcount, Boolean isSproutWelcome) {
        if (headcount != null) this.headcount = headcount;
        if (isSproutWelcome != null) this.isSproutWelcome = isSproutWelcome;
    }
}
