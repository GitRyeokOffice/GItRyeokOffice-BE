package com.example.gitryeokoffice.post.domain;

import com.example.gitryeokoffice.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공고-태그 매핑 엔티티
 * 공고에서 사용하는 기술 스택 표현
 */
@Entity
@Table(
    name = "post_tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_tag", columnNames = {"post_id", "tag_id"})
    },
    indexes = {
        @Index(name = "idx_tag_id", columnList = "tag_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 정적 팩토리 메서드
    public static PostTag create(Post post, Tag tag) {
        PostTag postTag = new PostTag();
        postTag.post = post;
        postTag.tag = tag;
        return postTag;
    }
}
