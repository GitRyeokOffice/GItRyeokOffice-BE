# 깃력사무소 DB 스키마 설계서 (ERD)

> **Version:** v1.1 (개선안 적용)
> **Last Updated:** 2025-12-23
> **Database:** MySQL 8.0

---

## 📋 목차
1. [ERD 다이어그램](#erd-다이어그램)
2. [테이블 상세 명세](#테이블-상세-명세)
3. [인덱스 전략](#인덱스-전략)
4. [제약조건](#제약조건)

---

## 🗂️ ERD 다이어그램

```
┌─────────────────────┐
│       users         │
├─────────────────────┤
│ PK  id              │
│ UQ  github_login    │
│     email           │
│     password        │
│     display_name    │
│     role_type       │
│     organization    │
│     position        │
│     project_exp_cnt │
│     is_sprout       │
│     profile_img_url │
│     bio             │
│     github_url      │
│     created_at      │
│     updated_at      │
└─────────────────────┘
         │ 1
         │
         ├──────┐
         │      │
         │ *    │ 1
┌────────┴──────┴──────┐       ┌──────────────────────┐
│   user_tags          │   *   │        tags          │
├──────────────────────┤───────├──────────────────────┤
│ PK,FK user_id        │       │ PK  id               │
│ PK,FK tag_id         │       │     name             │
│       created_at     │       │     type             │
└──────────────────────┘       │     created_at       │
                               └──────────────────────┘
                                        │ 1
                                        │
                                        │ *
                               ┌──────────────────────┐
                               │    post_tags         │
                               ├──────────────────────┤
                               │ PK,FK post_id        │
                               │ PK,FK tag_id         │
                               └──────────────────────┘
                                        │ 1
                                        │
┌─────────────────────┐                │
│ dev_vibe_results    │                │
├─────────────────────┤         ┌──────┴──────────────┐
│ PK  id              │         │       posts         │
│ UQ,FK user_id       │         ├─────────────────────┤
│     work_style      │         │ PK  id              │
│     activity_pattern│         │ FK  owner_user_id   │
│     time_of_day     │         │     title           │
│     summary         │         │     description     │
│     explain_json    │         │     goal_type       │
│     computed_at     │         │     exp_duration_wks│
└─────────────────────┘         │     deadline        │
                                │     view_count      │
                                │     status          │
                                │     created_at      │
                                │     updated_at      │
                                └─────────────────────┘
                                   │ 1        │ 1
                         ┌─────────┤          ├──────────┐
                         │ *       │          │ *        │
            ┌────────────┴───┐  ┌──┴──────────┴───┐  ┌──┴────────────┐
            │ applications   │  │ post_role_needs  │  │  invitations  │
            ├────────────────┤  ├──────────────────┤  ├───────────────┤
            │ PK  id         │  │ PK  id           │  │ PK  id        │
            │ FK  post_id    │  │ FK  post_id      │  │ FK  post_id   │
            │ FK  applicant  │  │     position     │  │ FK  sender    │
            │     message    │  │     headcount    │  │ FK  receiver  │
            │     status     │  │     is_sprout_ok │  │     message   │
            │     created_at │  │     created_at   │  │     status    │
            └────────────────┘  └──────────────────┘  │     created_at│
                                                      └───────────────┘
                                   │ 1
                                   │ *
                          ┌────────┴────────────┐
                          │ post_target_vibes   │
                          ├─────────────────────┤
                          │ PK  id              │
                          │ FK  post_id         │
                          │     axis            │
                          │     desired_label   │
                          └─────────────────────┘

                                   │ 1
                                   │ *
                          ┌────────┴────────────┐
                          │  post_comments      │
                          ├─────────────────────┤
                          │ PK  id              │
                          │ FK  post_id         │
                          │ FK  author_user_id  │
                          │ FK  parent_comment  │
                          │     content         │
                          │     is_deleted      │
                          │     created_at      │
                          │     updated_at      │
                          └─────────────────────┘

┌─────────────────────────┐
│   matching_scores       │ (추천 시스템용)
├─────────────────────────┤
│ PK  id                  │
│ FK  post_id             │
│ FK  user_id             │
│     score               │
│     match_reasons       │
│     calculated_at       │
└─────────────────────────┘
```

---

## 📊 테이블 상세 명세

### 1️⃣ users (사용자)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 사용자 고유 ID |
| github_login | VARCHAR(100) | UNIQUE, NOT NULL | GitHub 사용자명 (분석 데이터 수집 키) |
| email | VARCHAR(255) | UNIQUE, NULL | 이메일 (로그인/알림용) |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| display_name | VARCHAR(100) | NULL | 서비스 내 표시 이름 |
| role_type | ENUM | NOT NULL | DEVELOPER, NON_DEVELOPER |
| organization | VARCHAR(100) | NULL | 소속 (학교/회사) |
| position | ENUM | NULL | FE, BE, AI, MOBILE, DESIGN, PM, ETC |
| project_experience_count | INT | NULL DEFAULT 0 | 프로젝트 경험 횟수 |
| is_sprout | BOOLEAN | NOT NULL DEFAULT false | 새싹 개발자 여부 |
| profile_image_url | VARCHAR(500) | NULL | GitHub 프로필 이미지 URL |
| bio | TEXT | NULL | 자기소개 |
| github_url | VARCHAR(255) | NULL | GitHub 프로필 링크 |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일시 |

**비즈니스 규칙:**
- `github_login`은 GitHub API 호출의 핵심 키
- `role_type`이 `NON_DEVELOPER`면 GitHub 데이터 수집 스킵
- `is_sprout`가 true면 Dev-Vibe 분석 시 "새싹 개발자" 타입으로 분류

---

### 2️⃣ tags (기술스택 태그)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 태그 고유 ID |
| name | VARCHAR(50) | NOT NULL | 태그 이름 (예: Spring Boot, React) |
| type | ENUM | NOT NULL | TECH_STACK (향후 확장 가능) |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 생성일시 |

**제약조건:**
- `UNIQUE(name, type)`: 같은 타입 내에서 태그명 중복 방지

---

### 3️⃣ user_tags (사용자-태그 매핑)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| user_id | BIGINT | FK → users.id | 사용자 ID |
| tag_id | BIGINT | FK → tags.id | 태그 ID |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 연결일시 |

**복합 PK:** `(user_id, tag_id)`

---

### 4️⃣ dev_vibe_results (개발자 성향 분석 결과)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 결과 고유 ID |
| user_id | BIGINT | FK → users.id, UNIQUE | 사용자 ID (1:1 관계) |
| work_style | ENUM | NOT NULL | PLANNED, IMPROVISATION |
| activity_pattern | ENUM | NOT NULL | STEADY, FOCUS |
| time_of_day | ENUM | NOT NULL | MORNING, NIGHT |
| summary | VARCHAR(255) | NULL | AI 생성 한 줄 소개 |
| explain_json | JSON | NULL | 분석 근거 데이터 (ratio, 통계 등) |
| computed_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 분석 수행 일시 |

**explain_json 예시:**
```json
{
  "issue_pr_ratio": 0.65,
  "grass_density_percent": 42,
  "night_commit_ratio": 0.73,
  "total_commits_last_year": 245,
  "analysis_period": "2024-01-01 ~ 2024-12-31"
}
```

**비즈니스 규칙:**
- `user_id`는 UNIQUE → 사용자당 최신 분석 결과 1개만 저장
- 재분석 시 기존 레코드를 UPDATE

---

### 5️⃣ posts (팀빌딩 공고)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 공고 고유 ID |
| owner_user_id | BIGINT | FK → users.id | 공고 작성자 (팀장) |
| title | VARCHAR(200) | NOT NULL | 공고 제목 |
| description | TEXT | NULL | 공고 본문 |
| goal_type | ENUM | NOT NULL | AWARD, LAUNCH, EXPERIENCE |
| expected_duration_weeks | INT | NULL | 예상 프로젝트 기간 (주 단위) |
| deadline | DATE | NULL | 모집 마감일 |
| view_count | INT | NOT NULL DEFAULT 0 | 조회수 |
| status | ENUM | NOT NULL | OPEN, CLOSED |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일시 |

---

### 6️⃣ post_role_needs (공고별 모집 포지션)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 모집 역할 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| position | ENUM | NOT NULL | FE, BE, AI, MOBILE, DESIGN, PM, ETC |
| headcount | INT | NOT NULL DEFAULT 1 | 모집 인원 수 |
| is_sprout_welcome | BOOLEAN | NOT NULL DEFAULT false | 새싹 개발자 참여 가능 여부 |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 생성일시 |

---

### 7️⃣ post_target_vibes (공고별 선호 성향)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 성향 조건 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| axis | ENUM | NOT NULL | WORK_STYLE, ACTIVITY_PATTERN, TIME_OF_DAY |
| desired_label | VARCHAR(50) | NOT NULL | PLANNED/IMPROVISATION 등 |

**제약조건:**
- `UNIQUE(post_id, axis)`: 한 공고에서 같은 축은 1개만

---

### 8️⃣ post_tags (공고-태그 매핑)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| post_id | BIGINT | FK → posts.id | 공고 ID |
| tag_id | BIGINT | FK → tags.id | 태그 ID |

**복합 PK:** `(post_id, tag_id)`

---

### 9️⃣ applications (지원)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 지원 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| applicant_user_id | BIGINT | FK → users.id | 지원자 ID |
| message | TEXT | NULL | 지원 메시지 |
| status | ENUM | NOT NULL | APPLIED, ACCEPTED, REJECTED |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 지원일시 |

**제약조건:**
- `UNIQUE(post_id, applicant_user_id)`: 같은 공고에 중복 지원 방지

---

### 🔟 invitations (합류 제안)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 제안 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| sender_user_id | BIGINT | FK → users.id | 제안 발송자 (팀장) |
| receiver_user_id | BIGINT | FK → users.id | 제안 수신자 |
| message | TEXT | NULL | 제안 메시지 |
| status | ENUM | NOT NULL | SENT, ACCEPTED, DECLINED |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 제안일시 |

**제약조건:**
- `UNIQUE(post_id, receiver_user_id)`: 같은 공고에서 동일인에게 중복 제안 방지

---

### 1️⃣1️⃣ post_comments (공고 댓글)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 댓글 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| author_user_id | BIGINT | FK → users.id | 작성자 ID |
| parent_comment_id | BIGINT | FK → post_comments.id, NULL | 부모 댓글 ID (대댓글용) |
| content | TEXT | NOT NULL | 댓글 내용 |
| is_deleted | BOOLEAN | NOT NULL DEFAULT false | 삭제 여부 (soft delete) |
| created_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일시 |

---

### 1️⃣2️⃣ matching_scores (매칭 스코어 - 추천 시스템용)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 스코어 고유 ID |
| post_id | BIGINT | FK → posts.id | 공고 ID |
| user_id | BIGINT | FK → users.id | 추천 대상 개발자 ID |
| score | DECIMAL(5,2) | NOT NULL | 매칭 점수 (0.00 ~ 100.00) |
| match_reasons | JSON | NULL | 매칭 근거 ["작업방식 일치", "시간대 유사"] |
| calculated_at | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 계산 일시 |

**제약조건:**
- `UNIQUE(post_id, user_id)`: 공고-사용자 조합당 1개만
- `CHECK(score >= 0 AND score <= 100)`

**match_reasons 예시:**
```json
[
  "작업방식 일치 (계획형)",
  "활동패턴 유사 (지속형)",
  "기술스택 3개 일치 (Spring Boot, React, MySQL)"
]
```

---

## 🔍 인덱스 전략

### 성능 최적화를 위한 인덱스

```sql
-- 공고 검색 최적화
CREATE INDEX idx_posts_status ON posts(status);
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
CREATE INDEX idx_posts_deadline ON posts(deadline);

-- 지원/제안 상태 조회 최적화
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_applications_applicant ON applications(applicant_user_id);
CREATE INDEX idx_invitations_status ON invitations(status);
CREATE INDEX idx_invitations_receiver ON invitations(receiver_user_id);

-- Dev-Vibe 분석 결과 조회
CREATE INDEX idx_dev_vibe_computed_at ON dev_vibe_results(computed_at);

-- 댓글 조회 최적화
CREATE INDEX idx_comments_post_id ON post_comments(post_id);
CREATE INDEX idx_comments_created_at ON post_comments(created_at DESC);

-- 매칭 스코어 조회 최적화
CREATE INDEX idx_matching_scores_post ON matching_scores(post_id, score DESC);
```

---

## ⚠️ 제약조건 요약

### UNIQUE 제약조건
- `users.github_login`
- `users.email`
- `dev_vibe_results.user_id`
- `tags(name, type)`
- `user_tags(user_id, tag_id)`
- `post_tags(post_id, tag_id)`
- `post_target_vibes(post_id, axis)`
- `applications(post_id, applicant_user_id)`
- `invitations(post_id, receiver_user_id)`
- `matching_scores(post_id, user_id)`

### ON DELETE 정책
- `user_tags`, `dev_vibe_results`: ON DELETE CASCADE (사용자 삭제 시 함께 삭제)
- `applications`, `invitations`: ON DELETE CASCADE (공고 삭제 시 함께 삭제)
- `post_comments`: parent_comment_id ON DELETE SET NULL (부모 댓글 삭제 시 최상위 댓글로)

---

## 📝 변경 이력

### v1.1 (2025-12-23)
- ✅ `users` 테이블에 `email`, `profile_image_url`, `bio`, `github_url`, `created_at`, `updated_at` 추가
- ✅ `posts` 테이블에 `expected_duration_weeks`, `deadline`, `view_count` 추가
- ✅ `post_comments`에 `is_deleted`, `updated_at` 추가
- ✅ `matching_scores` 테이블 신규 추가 (추천 시스템용)
- ✅ 인덱스 전략 문서화
- ✅ Enum 값 일관성 통일 (`DESIGN` → `DESIGNER` 통일 검토 필요)

### v1.0 (Initial)
- 초기 PRD 기반 스키마 설계
