# 🍣 QuizSushi - Backend

**QuizSushi**는 사용자가 직접 퀴즈를 만들고 풀며 지식을 나누는 플랫폼입니다. 이 저장소는 Spring Boot 기반의 백엔드 코드로, 실서비스 운영을 목표로 설계되었습니다. 
OAuth2 로그인부터 관리자 전용 대시보드, Redis 기반 토큰 관리, Docker 배포 자동화까지 포함되어 있습니다.

## 주요 특징

- **JWT 인증과 토큰 자동 갱신**: 액세스 토큰이 만료되면 Redis에 저장된 리프레시 토큰을 이용해 서버 필터가 새 토큰을 발급합니다. 리프레시 토큰은 `회원ID + User-Agent` 조합으로 검증하여 다른 환경에서의 재사용을 차단합니다.
- **봇 접근 차단**: `@RejectBot` 애노테이션과 `UserAgentAspect`가 의심스러운 User-Agent를 탐지하여 요청을 거부합니다.
- **두 가지 보안 체인**: API용 JWT 기반 필터 체인과 관리자 페이지용 세션 기반 필터 체인을 분리하여 적용합니다. 관리자 계정은 `ROOT > ADMIN > MANAGER > VIEWER` 순으로 계층화된 권한을 가집니다.
- **MinIO 미디어 관리**: 업로드된 파일은 임시 폴더(`public/tmp`)에 저장됐다가 퀴즈 저장 시 `public/quiz/{mediaKey}` 경로로 이동하며, 본문 내 임시 링크도 자동으로 재작성됩니다.
- **AI 문제 생성**: `AiService`가 로컬 AI 서버와 통신하여 퀴즈 문제를 생성합니다. 프롬프트는 DB에서 관리하여 업데이트할 수 있습니다.
- **로깅 커스터마이징**: `EmojiLevelConverter`를 사용해 로그 레벨을 이모지로 표시하고, `RequestLoggingAspect`와 `LoggingInterceptor`가 모든 요청과 메서드 실행 시간을 기록합니다.
- **Docker 기반 CI/CD**: GitHub Actions가 이미지를 빌드해 DockerHub로 푸시하고, 원격 서버에 배포합니다.

## 프로젝트 구조

```
quizsushi-be/
├── src
│   ├── main
│   │   ├── java/com/cmdlee/quizsushi
│   │   │   ├── admin/          # 관리자 기능
│   │   │   ├── member/         # 회원 도메인
│   │   │   ├── quiz/           # 퀴즈 도메인
│   │   │   ├── report/         # 신고 도메인
│   │   │   ├── global/         # 공통 설정, AOP, 보안
│   │   │   └── minio/          # 파일 업로드 모듈
│   │   └── resources
│   │       ├── application.yml
│   │       └── init.sql
└── build.gradle
```

## 인증 흐름

1. 사용자가 로그인하면 서버가 AccessToken과 RefreshToken을 모두 쿠키로 내려줍니다.
2. AccessToken 만료 시 `JwtAuthenticationFilter`가 RefreshToken을 읽어 Redis에서 일치하는 값을 조회합니다.
3. 저장된 User-Agent와 현재 요청의 User-Agent가 다르면 `TOKEN_CLIENT_MISMATCH` 오류로 갱신을 거부합니다.
4. 검증이 완료되면 새로운 AccessToken을 생성해 `Set-Cookie` 헤더로 응답합니다.

## 관리자 보안 체계

- `/api/**` 경로는 JWT 기반의 Stateless 보안 필터 체인을 사용합니다.
- `/cmdlee-qs/**` 경로는 세션 로그인을 사용하며, 로그인 성공 시 `/cmdlee/dashboard`로 리다이렉트합니다.
- `RoleHierarchy` Bean을 통해 상위 권한이 하위 권한을 자동으로 포함하도록 구성했습니다.
- 접근 거부 시 요청 정보와 인증 주체를 상세 로그로 남깁니다.

## 배포 전략

- `Dockerfile`과 `docker-compose`를 이용해 애플리케이션과 Redis를 컨테이너로 구동합니다.
- `.github/workflows/backend.yml`에서 이미지 빌드 후 DockerHub에 푸시하고, SSH로 운영 서버에 접속해 새 컨테이너를 실행합니다.
- 운영용 환경 변수 파일은 서버에만 존재하며 Git에는 포함하지 않습니다.

## 로컬 개발 방법

1. 이 저장소를 클론한 뒤 `.env.example`을 `.env`로 복사하여 환경 변수를 입력합니다.
2. PostgreSQL과 Redis를 로컬에서 실행하거나 Docker로 준비합니다.
3. `./gradlew bootRun`으로 서버를 실행하거나, 다음 명령으로 이미지 빌드 후 실행할 수 있습니다.
   ```bash
   docker build -t quizsushi-be .
   docker run --env-file .env -p 8080:8080 quizsushi-be
   ```
4. 초기 데이터가 필요하면 `src/main/resources/init.sql`을 DB에 적용하세요.

## 기술 스택

| 분야       | 사용 기술                                                    |
|------------|---------------------------------------------------------------|
| Language   | Java 17                                                      |
| Framework  | Spring Boot, Spring Security, Spring Data JPA               |
| DB         | PostgreSQL, Redis                                           |
| Infra      | Docker, Docker Compose, NGINX, MinIO                         |
| Build Tool | Gradle                                                       |
| CI/CD      | GitHub Actions + DockerHub + Self-hosted Ubuntu Server       |
---

## 🗂 커밋 컨벤션

| 타입 | 설명 |
|------|------|
| feat | 새로운 기능 추가 또는 기존 기능 명세 변경 |
| fix | 버그 수정 또는 의도한 동작이 안 되던 부분 수정 |
| refactor | 리팩토링 (기능 변화 없이 코드 구조/품질 개선) |
| chore | 빌드, 설정 파일, 인프라 관련 잡일 |
| docs | 문서 또는 주석만 변경한 경우 |
| test | 테스트 코드 추가 또는 수정 |
| style | 코드 포맷팅, 세미콜론, 공백 등 변경 (기능 변화 없음) |
| perf | 성능 개선 |
| ci | CI 설정 및 관련 스크립트 변경 |

---

## 💬 기타

- Spring Security + Redis + Docker를 바탕으로 실제 운영 환경에서 고려해야 할 인증 흐름, 세션 관리, 배포 자동화를 구성하는 데 중점을 두었습니다.
- 실시간 퀴즈, 포인트 시스템, 갓챠 등은 추후 업데이트 예정입니다.
