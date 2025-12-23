1. 프로젝트 개요
   깃력사무소 (GitRyeokOffice): GitHub 활동 로그 기반 AI 성향 분석 팀빌딩 플랫폼. 사용자의 GitHub 데이터를 1차 가공하여 OpenAI API로 전달하고, AI가 분석한 'Dev-Vibe' 카드(성향 분석) 및 매칭 사유를 제공함.

2. 기술 스택
   Back-end: Java 21, Spring Boot 3.5.x

Data: Spring Data JPA, MySQL 8.0

AI: OpenAI API (GPT-4o)

External API: GitHub REST API (Octokit 스타일 또는 WebClient 사용)

3. 핵심 비즈니스 로직 (Dev-Vibe 3축)
   계획형 vs 즉흥형: 이슈 생성 빈도 및 PR 연동률 기반

지속형 vs 몰입형: 잔디 밀도 및 연속 커밋 일수 기반

아침형 vs 저녁형: 커밋 타임스탬프 기반 (06:00 기준)

4. 코딩 가이드
   계층 구조: controller -> service -> repository (단순화된 3계층)

DTO: record 사용 필수

네이밍: 축약어를 지양하고 의도가 명확한 한글 주석을 필요한 곳에 추가

Exception: 도메인별 복잡한 Enum 대신 RuntimeException 기반의 공통 예외 처리 혹은 단순한 ErrorResponse 반환

Lombok: @Getter, @RequiredArgsConstructor 등 적극 활용

5. Git 규칙 (해커톤 간소화)
   브랜치 전략: main 브랜치 기반, 작업 시 개인별 브랜치 사용 (예: hyerimh/feat-1)

커밋 메시지: Conventional Commits 준수 (한글 작성)

형식: feat: 기능 설명, fix: 버그 수정 등

주의 사항:

AI 도구 흔적 금지: 커밋 메시지에 🤖 Generated with [Claude Code] 또는 Co-Authored-By: Claude 등 Claude 관련 문구를 절대 포함하지 말 것.

모든 커밋은 사람이 직접 작성한 것처럼 자연스러워야 함.

6. 개발 필수 체크리스트
   보안: GitHub Token, OpenAI API Key는 반드시 .env 또는 application-secret.yml로 관리 (git 추적 금지)

AI 연동: GitHub 데이터를 문자열/JSON으로 요약하여 OpenAI에 전송할 때 토큰 효율성을 고려할 것

로직: username 하나만으로 GitHub 공개 이벤트를 긁어오는 로직이 핵심

7. 실행 명령어
   빌드/실행: ./gradlew bootRun

테스트: ./gradlew test
