# 🍣 QuizSushi - Backend

**QuizSushi**는 사용자가 직접 퀴즈를 만들고 풀며, 초밥처럼 다양한 지식을 맛보는 참여형 퀴즈 플랫폼입니다.  
Spring Boot 기반 REST API 서버로, 인증과 퀴즈 생성·풀이, 신고 및 통계 기능을 포함합니다.

---

## ✅ 기술 스택

| 분야       | 사용 기술 |
|------------|------------------------------------------------|
| Language   | Java 17 |
| Framework  | Spring Boot, Spring Security, Spring Data JPA |
| DB         | PostgreSQL, Redis |
| Build Tool | Gradle |
| Infra      | Docker, Docker Compose, NGINX, MinIO |
| CI/CD      | GitHub Actions + DockerHub + Self-hosted Ubuntu Server |

---

## 🧩 주요 기능

- 회원 가입 및 소셜 로그인(Google, Kakao)
- 문제 출제 / 풀이 / 신고 시스템
- 관리자 전용 대시보드 (통계, 신고 처리 등)
- OAuth2 인증 기반 로그인과 권한 관리
- Redis 기반 JWT 인증 및 자동 토큰 재발급
- MinIO 기반 이미지 업로드 및 링크 재작성
- **봇 접근 차단**: `@RejectBot` 애노테이션 + `UserAgentAspect` 활용
- **AI 기반 문제 생성**: `AiService`가 WebClient로 로컬 AI 서버와 통신
- **이모지 로그 및 요청 로깅**: `EmojiLevelConverter`, `RequestLoggingAspect`
- **Docker 기반 배포 자동화**: GitHub Actions에서 이미지 빌드 및 서버 자동 배포
- 관리자 Role 계층 구조: `ROOT > ADMIN > MANAGER > VIEWER`

> ⚠️ 실시간 퀴즈 대결 및 포인트 보상 시스템은 아직 구현되지 않았습니다.

---

## 🔐 인증 구조 요약

- **AccessToken**: JWT / `Authorization: Bearer` 헤더 사용
- **RefreshToken**: JWT / `HttpOnly + Secure + SameSite=Strict` 쿠키로 전송
- **토큰 식별 및 검증**: Redis에 `회원ID + User-Agent + IP` 기준으로 저장 및 갱신 검증
- **자동 갱신**: AccessToken 만료 시 서버 필터가 RefreshToken으로 재발급

---

## 🧑‍💻 사용자 vs 관리자 인증 구조 비교

QuizSushi는 **사용자와 관리자에 대해 인증/인가 구조를 분리**하여 역할에 맞는 보안 정책을 적용했습니다.

| 항목 | 일반 사용자 (User) | 관리자 (Admin) |
|------|--------------------|----------------|
| **인증 방식** | 소셜 로그인 (OAuth2) | ID/PW 기반 폼 로그인 |
| **AccessToken 전송** | `Authorization: Bearer` 헤더 | 동일 |
| **RefreshToken 전송** | `HttpOnly + Secure` 쿠키 | 동일 |
| **보안 필터 체인** | `/api/**` 경로에 전용 필터 체인 적용 | `/cmdlee/**` 경로에 별도 필터 체인 적용 |
| **세션 구조** | Stateless (무세션) | 동일 (JWT 기반 무세션) |
| **권한 계층** | 없음 (기본 사용자) | `ROOT > ADMIN > MANAGER > VIEWER` |
| **리디렉션 처리** | 소셜 콜백 처리 | 로그인/로그아웃 시 커스텀 URI 처리 |
| **주요 사용 목적** | 퀴즈 풀이/출제 | 신고 처리, 통계 분석 등 운영 |

> 🔐 관리자 인증도 JWT 기반으로 통일하여 관리 편의성과 확장성을 고려하였습니다.  
> URL 패턴 기반의 필터 분리 전략으로 보안과 유지보수성을 강화했습니다.

---

## 🗂 프로젝트 구조

```
quizsushi-be/
├── src
│   ├── main
│   │   ├── java/com/cmdlee/quizsushi
│   │   │   ├── admin/       # 관리자 기능
│   │   │   ├── member/      # 회원 도메인
│   │   │   ├── quiz/        # 퀴즈 도메인
│   │   │   ├── report/      # 신고 도메인
│   │   │   ├── global/      # 공통 설정 및 유틸
│   │   │   └── minio/       # 이미지 업로드 기능
│   │   └── resources
│   │       ├── application.yml
│   │       └── init.sql
└── build.gradle
```

---

## 🚀 배포 전략

### ✅ 운영 서버 구성

- **Docker 기반 배포**  
  백엔드와 Redis를 각각 컨테이너로 관리하며, `.env.prod`를 통해 민감정보 관리

- **도커 컴포즈 예시**
  ```yaml
  services:
    backend:
      image: dlaudfuf33/quizsushi-be:${IMAGE_TAG}
      env_file:
        - .env.prod
      ports:
        - "8080:8080"
      depends_on:
        - redis
      networks:
        - quizsushi-net
    redis:
      image: redis:7
  ```

### ✅ CI/CD 구성

- GitHub Actions 워크플로우는 `main` 브랜치 푸시 시 다음을 자동 수행:
  1. Docker 이미지 빌드 및 DockerHub에 푸시
  2. 운영 서버 SSH 접속 → 기존 컨테이너 중단 및 새 이미지로 재실행
  3. 배포 버전 로그 기록

---

## 🧪 로컬 개발 환경

```bash
# 1. 환경 변수 설정
cp .env.example .env

# 2. 의존성 설치 및 실행
./gradlew build
./gradlew bootRun

# 또는 Docker로 실행
docker build -t quizsushi-be .
docker run --env-file .env -p 8080:8080 quizsushi-be
```

- 로컬 PostgreSQL 및 Redis는 직접 실행하거나 Docker를 이용
- 초기 DB 데이터: `src/main/resources/init.sql` 참고

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
