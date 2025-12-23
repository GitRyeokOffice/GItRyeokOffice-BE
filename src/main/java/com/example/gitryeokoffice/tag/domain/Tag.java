package com.example.gitryeokoffice.tag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 태그 엔티티
 * 기술 스택 등을 표현하는 태그 정보
 */
@Entity
@Table(
    name = "tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_tags_name_type", columnNames = {"name", "type"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // 태그 이름 (예: Spring Boot, React)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type; // 태그 타입

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드
    public static Tag create(String name, TagType type) {
        Tag tag = new Tag();
        tag.name = name;
        tag.type = type;
        return tag;
    }

    // 태그명 수정
    public void updateName(String name) {
        this.name = name;
    }
}
