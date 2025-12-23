package com.example.gitryeokoffice.devvibe.domain;

import com.example.gitryeokoffice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Dev-Vibe 분석 결과 엔티티
 * GitHub 활동 데이터 기반 개발자 성향 분석 결과
 */
@Entity
@Table(
    name = "dev_vibe_results",
    indexes = {
        @Index(name = "idx_computed_at", columnList = "computedAt"),
        @Index(name = "idx_work_style", columnList = "workStyle"),
        @Index(name = "idx_activity_pattern", columnList = "activityPattern"),
        @Index(name = "idx_time_of_day", columnList = "timeOfDay")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevVibeResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; // 사용자 (1:1 관계)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkStyle workStyle; // 작업 방식: PLANNED, IMPROVISATION

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityPattern activityPattern; // 활동 패턴: STEADY, FOCUS

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeOfDay timeOfDay; // 주 활동 시간: MORNING, NIGHT

    @Column(length = 255)
    private String summary; // AI 생성 한 줄 소개

    @Column(columnDefinition = "JSON")
    private String explainJson; // 분석 근거 데이터 (JSON 형태)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime computedAt; // 분석 수행 일시

    // 정적 팩토리 메서드
    public static DevVibeResult create(
        User user,
        WorkStyle workStyle,
        ActivityPattern activityPattern,
        TimeOfDay timeOfDay,
        String summary,
        String explainJson
    ) {
        DevVibeResult result = new DevVibeResult();
        result.user = user;
        result.workStyle = workStyle;
        result.activityPattern = activityPattern;
        result.timeOfDay = timeOfDay;
        result.summary = summary;
        result.explainJson = explainJson;
        return result;
    }

    // 분석 결과 재갱신 (사용자가 "내 카드 갱신하기" 버튼 클릭 시)
    public void update(
        WorkStyle workStyle,
        ActivityPattern activityPattern,
        TimeOfDay timeOfDay,
        String summary,
        String explainJson
    ) {
        this.workStyle = workStyle;
        this.activityPattern = activityPattern;
        this.timeOfDay = timeOfDay;
        this.summary = summary;
        this.explainJson = explainJson;
    }

    // Dev-Vibe 코드 생성 (예: PSM, IFN 등)
    public String getVibeCode() {
        StringBuilder code = new StringBuilder();
        code.append(workStyle == WorkStyle.PLANNED ? "P" : "I");
        code.append(activityPattern == ActivityPattern.STEADY ? "S" : "F");
        code.append(timeOfDay == TimeOfDay.MORNING ? "M" : "N");
        return code.toString();
    }

    // Dev-Vibe 타입명 생성 (예: "안정 루틴형", "심야 몰입형" 등)
    public String getVibeTypeName() {
        String code = getVibeCode();
        return switch (code) {
            case "PSM" -> "안정 루틴형";
            case "PSN" -> "정숙 빌드형";
            case "PFM" -> "집중 설계형";
            case "PFN" -> "야간 구상형";
            case "ISM" -> "유연 흐름형";
            case "ISN" -> "완만 반복형";
            case "IFM" -> "즉시 실행형";
            case "IFN" -> "심야 몰입형";
            default -> "분석 중";
        };
    }
}
