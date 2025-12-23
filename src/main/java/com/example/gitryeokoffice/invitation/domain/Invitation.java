package com.example.gitryeokoffice.invitation.domain;

import com.example.gitryeokoffice.post.domain.Post;
import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 합류 제안 엔티티
 * 팀장이 개발자에게 보내는 합류 제안
 */
@Entity
@Table(
    name = "invitations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_receiver", columnNames = {"post_id", "receiver_user_id"})
    },
    indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_receiver_user_id", columnList = "receiver_user_id"),
        @Index(name = "idx_created_at", columnList = "createdAt")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender; // 제안 발송자 (팀장)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiver; // 제안 수신자

    @Column(columnDefinition = "TEXT")
    private String message; // 제안 메시지

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.SENT; // 제안 상태

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 제안일시

    // 정적 팩토리 메서드
    public static Invitation create(Post post, User sender, User receiver, String message) {
        Invitation invitation = new Invitation();
        invitation.post = post;
        invitation.sender = sender;
        invitation.receiver = receiver;
        invitation.message = message;
        invitation.status = InvitationStatus.SENT;
        return invitation;
    }

    // 제안 수락
    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }

    // 제안 거절
    public void decline() {
        this.status = InvitationStatus.DECLINED;
    }

    // 수신자 본인인지 확인 (수락/거절 권한)
    public boolean isReceivedBy(User user) {
        return this.receiver.getId().equals(user.getId());
    }

    // 발송자 본인인지 확인
    public boolean isSentBy(User user) {
        return this.sender.getId().equals(user.getId());
    }
}
