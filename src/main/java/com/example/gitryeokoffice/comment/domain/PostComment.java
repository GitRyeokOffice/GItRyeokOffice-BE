package com.example.gitryeokoffice.comment.domain;

import com.example.gitryeokoffice.post.domain.Post;
import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 공고 댓글 엔티티
 * 공고에 대한 댓글/대댓글
 */
@Entity
@Table(
    name = "post_comments",
    indexes = {
        @Index(name = "idx_post_id", columnList = "post_id"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_parent_comment_id", columnList = "parent_comment_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User author; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment; // 부모 댓글 (대댓글용)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 댓글 내용

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted = false; // 삭제 여부 (soft delete)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    // 정적 팩토리 메서드 - 일반 댓글
    public static PostComment create(Post post, User author, String content) {
        PostComment comment = new PostComment();
        comment.post = post;
        comment.author = author;
        comment.content = content;
        comment.isDeleted = false;
        return comment;
    }

    // 정적 팩토리 메서드 - 대댓글
    public static PostComment createReply(Post post, User author, PostComment parentComment, String content) {
        PostComment comment = new PostComment();
        comment.post = post;
        comment.author = author;
        comment.parentComment = parentComment;
        comment.content = content;
        comment.isDeleted = false;
        return comment;
    }

    // 댓글 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // 댓글 삭제 (soft delete)
    public void delete() {
        this.isDeleted = true;
        this.content = "삭제된 댓글입니다.";
    }

    // 본인 댓글인지 확인
    public boolean isWrittenBy(User user) {
        return this.author.getId().equals(user.getId());
    }

    // 대댓글인지 확인
    public boolean isReply() {
        return this.parentComment != null;
    }
}
