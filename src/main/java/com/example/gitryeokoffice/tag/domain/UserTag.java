package com.example.gitryeokoffice.tag.domain;

import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 사용자-태그 매핑 엔티티
 * 사용자가 보유한 기술 스택을 표현
 */
@Entity
@Table(
    name = "user_tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_tag", columnNames = {"user_id", "tag_id"})
    },
    indexes = {
        @Index(name = "idx_tag_id", columnList = "tag_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드
    public static UserTag create(User user, Tag tag) {
        UserTag userTag = new UserTag();
        userTag.user = user;
        userTag.tag = tag;
        return userTag;
    }
}
