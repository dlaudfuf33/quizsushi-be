# 🍣 QuizSushi - Backend

**QuizSushi**는 사용자가 직접 퀴즈를 만들고 풀며 지식을 나누는 플랫폼입니다. AI를 통해 문제를 생성하거나, 실시간 서바이벌 퀴즈 챌린지에 참여하며 지식을 쌓고 다른 사람들과 경쟁할 수 있습니다.
<img width="800" height="650" alt="main" src="https://github.com/user-attachments/assets/336586b3-5ab2-41c4-b246-87ff26d2adda" />


👉 **실서비스**: [https://quizsushi.cmdlee.com/](https://quizsushi.cmdlee.com/)

<br>

## 📖 프로젝트 개요

-   **프로젝트명**: QuizSushi
-   **목적**: 사용자들이 다양한 주제의 퀴즈를 생성 및 공유하고, 실시간 퀴즈 챌린지에 참여하며 경쟁하는 지식 엔터테인먼트 플랫폼 제공
<table>
  <tr>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/2246adf1-c7b1-4de6-b0fc-e3284d30fc78" alt="스크린샷1"/>
    </td>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/ae11b79f-e135-4997-8b8f-13975bc76e82" alt="스크린샷2"/>
    </td>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/18af0133-bf05-4e68-a35f-27991a610d9a" alt="스크린샷3"/>
    </td>
  </tr>
</table>
<br>

<table>
  <tr>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/f9e7c304-a6f2-4d65-9988-cbe6b1d25b4e" />
    </td>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/2a3b885b-1739-4ca4-900b-42e9b8ad2067" />
    </td>
  </tr>
</table>
<br>

<table>
  <tr>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/f89a5b98-4a18-44a5-b252-153d310d369f" />
    </td>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/14ffec11-08e6-4ac4-bc27-ff773065223d" />
    </td>
    <td align="center">
      <img width="300" src="https://github.com/user-attachments/assets/74d5092b-a444-4f7b-b77f-a1688874e740" />
    </td>
  </tr>
</table>

<br>

## ✨ 주요 기능

-   **AI 퀴즈 생성**: 텍스트만으로 간편하게 객관식, 주관식 퀴즈를 생성합니다.
-   **실시간 서바이벌 퀴즈 챌린지**: 여러 사용자와 실시간으로 퀴즈 대결을 펼치는 서바이벌 게임 모드입니다.
-   **사용자 생성 퀴즈 및 풀이**: 누구나 자신만의 퀴즈를 만들어 공유하고, 다른 사람의 퀴즈를 풀 수 있습니다.
-   **안전한 인증 시스템**: OAuth2 소셜 로그인과 JWT를 결합하여 안전하고 편리한 인증을 제공합니다.
-   **관리자 대시보드**: 사용자, 콘텐츠, 신고, 서비스 통계 등을 관리하는 전용 대시보드를 제공합니다.
-   **MinIO를 활용한 미디어 관리**: 퀴즈에 포함되는 이미지, 영상 등 미디어 파일을 효율적으로 처리합니다.

<br>

## 🛠️ 개발 환경

| 카테고리 | 라이브러리 | 설명 |
| :--- | :--- | :--- |
| **Framework** | Spring Boot (v3.5.0) | Spring 기반 웹 애플리케이션 프레임워크 |
| | Spring Security | 인증 및 권한 관리 라이브러리 |
| | Spring Data JPA | ORM(Object Relational Mapping) 데이터 처리 |
| **Database** | PostgreSQL | 운영 관계형 데이터베이스 |
| | Redis | Refresh Token, 실시간 챌린지 세션 등 캐시/상태 저장 |
| | H2 | 테스트용 인메모리 데이터베이스 |
| **Security** | JWT (io.jsonwebtoken) | JSON Web Token 기반 인증 및 인가 |
| | Spring OAuth2 Client | OAuth2 기반 소셜 로그인(Google, Kakao) |
| **AI** | Spring AI | Spring 애플리케이션에 AI 기능 통합 |
| | Vertex AI Gemini | Google의 생성형 AI 모델 |
| **Infrastructure** | MinIO | S3 호환 오브젝트 스토리지 |
| | Docker | 컨테이너 기반 애플리케이션 배포 |
| **API & Docs** | SpringDoc OpenAPI | Swagger (v3) 기반 API 문서 자동화 |
| **Build Tool** | Gradle | 의존성 관리 및 빌드 자동화 도구 |
| **CI/CD** | GitHub Actions | 소스 코드 빌드, 테스트, 배포 자동화 |

<br>

## 📐 설계 방향

QuizSushi 백엔드는 최신 Spring Boot 기술을 기반으로 **모듈화되고 확장 가능한 구조**를 지향합니다. DDD(도메인 주도 설계) 사상을 일부 차용하여 핵심 도메인의 복잡성을 관리하고, 전략(Strategy), 어댑터(Adapter), 팩토리(Factory) 등 다양한 디자인 패턴을 적용하여 코드의 유연성과 재사용성을 높였습니다.

### 🔐 인증 및 권한 관리

-   **이중 보안 체계**:
    -   **API (`/api/**`)**: JWT 기반의 무상태(Stateless) 인증 필터를 적용합니다.
    -   **관리자 페이지 (`/cmdlee-qs/**`)**: 세션(Session) 기반의 폼 로그인 방식을 적용하여 명확히 분리했습니다.
-   **안전한 토큰 관리**:
    -   Access Token은 만료 시간을 짧게 설정하고, Refresh Token을 사용하여 자동으로 갱신합니다.
    -   Refresh Token은 Redis에 `사용자 ID` 및 `User-Agent`와 함께 저장하여, 다른 환경에서의 토큰 탈취 및 재사용을 방지합니다.
-   **계층형 권한**: Spring Security의 `RoleHierarchy`를 사용하여 `ROOT > ADMIN > MANAGER > VIEWER` 순으로 권한을 계층화하고, 상위 역할이 하위 역할의 권한을 자동으로 상속받도록 설계했습니다.
-   **봇(Bot) 접근 차단**: `@RejectBot` 어노테이션과 AOP를 활용하여 `curl`, `python-requests` 등 자동화된 스크립트의 접근을 User-Agent 기반으로 차단합니다.

### ⚡ 실시간 아키텍처 (WebSocket & Redis)

-   **WebSocket**: `STOMP` 프로토콜을 사용하여 실시간 퀴즈 챌린지의 상태(게임 시작, 문제 출제, 정답 제출, 결과 발표 등)를 클라이언트와 양방향으로 통신합니다.
-   **Redis**:
    -   **세션 상태 저장**: 실시간으로 변하는 챌린지 게임(`ChallengeSession`)의 모든 상태를 Redis에 저장하여, 서버가 여러 대여도 상태를 공유할 수 있는 확장성 있는 구조를 갖추었습니다.
    -   **매칭 큐**: Redis의 `Sorted Set`을 대기열로 활용하여 챌린지 참가 희망자를 관리하고, 스케줄러를 통해 주기적으로 매칭을 시도합니다.
    -   **리더보드**: `Sorted Set`을 사용하여 챌린지 게임의 실시간 점수 랭킹을 관리합니다.

### 🤖 AI 연동 설계: 목적에 따른 이중 전략 (Dual-Strategy)

QuizSushi는 AI 모델을 사용하는 목적에 따라 두 가지 다른 아키텍처를 적용하여 유연성과 효율성을 모두 확보했습니다.

**1. Ollama 연동 (어댑터 패턴 적용)**
자체 호스팅되거나 교체 가능한 로컬 AI 모델(Llama3, Mistral 등)을 위해 **어댑터(Adapter) 패턴**을 적용했습니다.

-   **유연한 모델 교체**: `AiModelAdapter` 인터페이스를 통해 모든 모델을 동일한 방식으로 호출합니다. `CodeLlama` 등 새로운 모델을 추가하고 싶을 때, 기존 코드를 수정하지 않고 새로운 어댑터만 구현하여 `AiModelRouter`에 등록하면 됩니다.
-   **인프라 추상화**: `AiInstanceRouter`가 여러 AI 서버 인스턴스의 주소를 관리하며 라운드-로빈 방식으로 요청을 분산합니다. 이를 통해 AI 추론 서버의 부하를 분산하고 수평적으로 확장할 수 있습니다.
-   **사용처**: 일반 사용자의 AI 퀴즈 생성 기능에 사용됩니다.

**2. Gemini 연동 (Spring AI 직접 통합)**
클라우드 기반의 관리형 서비스인 Google Vertex AI Gemini 연동에는 **Spring AI 프레임워크를 직접 활용**합니다.

-   **라이브러리 활용 극대화**: Spring AI의 `VertexAiGeminiChatModel` 자체가 이미 강력한 추상화(어댑터)를 제공하므로, 이를 다시 감싸지 않고 직접 사용하여 코드 중복을 피하고 프레임워크의 모든 기능을 활용합니다.
-   **최적화된 통합**: 인증, API 호출, 재시도 로직 등 플랫폼에 특화된 기능들을 Spring AI에 위임하여 안정적이고 간결한 코드를 유지합니다.
-   **사용처**: 고품질의 문제가 요구되는 실시간 **퀴즈 챌린지**의 문제를 생성하는 데 특화되어 사용됩니다.

이처럼 QuizSushi는 연동 대상의 특성(자체 호스팅 vs 클라우드 서비스)과 사용 목적에 맞춰 가장 효율적인 아키텍처를 각각 선택하여 적용한 실용적인 설계를 갖추고 있습니다.

### 🗄️ 엔티티 설계 및 데이터 관리

-   JPA를 활용하여 도메인 모델을 설계했으며, `@EntityGraph` 및 `fetch join`을 적극적으로 사용하여 N+1 문제를 방지하고 조회 성능을 최적화했습니다.
-   `CascadeType.ALL`과 `orphanRemoval=true` 옵션을 활용하여 퀴즈 삭제 시 관련된 질문, 풀이 기록, 평점 등이 연쇄적으로 삭제되도록 하여 데이터 정합성을 유지합니다.

### ⚠️ 예외 처리

-   `ErrorCode` Enum 클래스를 통해 애플리케이션에서 발생할 수 있는 모든 예외 상황(코드, 메시지, HTTP 상태)을 중앙에서 체계적으로 관리합니다.
-   `@RestControllerAdvice`를 사용한 `GlobalExceptionHandler`가 모든 예외를 처리하여 일관된 JSON 형식의 에러 응답을 반환합니다.

<br>

## 🗂️ 커밋 컨벤션

| 타입 | 설명 |
| :--- | :--- |
| `feat` | 새로운 기능 추가 또는 기존 기능 명세 변경 |
| `fix` | 버그 수정 또는 의도한 동작이 안 되던 부분 수정 |
| `refactor` | 리팩토링 (기능 변화 없이 코드 구조/품질 개선) |
| `chore` | 빌드, 설정 파일, 인프라 관련 잡일 |
| `docs` | 문서 또는 주석만 변경한 경우 |
| `test` | 테스트 코드 추가 또는 수정 |
| `style` | 코드 포맷팅, 세미콜론, 공백 등 변경 (기능 변화 없음) |
| `perf` | 성능 개선 |
| `ci` | CI 설정 및 관련 스크립트 변경 |

<br>

## 📜 API 명세 (Swagger 기반)

-   **Swagger UI (로컬 테스트)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
-   SpringDoc(OpenAPI 3.0)을 활용하여 REST API 명세를 자동화하였으며, 위 주소에서 API 문서를 확인하고 직접 테스트해볼 수 있습니다.

<br>

## ✅ 주요 API 목록

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/ai/quizzes/questions` | AI를 통해 퀴즈 문제 생성 |
| `POST` | `/api/quizzes` | 사용자 퀴즈 생성 |
| `GET` | `/api/quizzes/{id}` | 퀴즈 상세 정보 조회 |
| `POST` | `/api/quizzes/{id}/results` | 퀴즈 풀이 결과 제출 |
| `GET` | `/api/auth/{provider}/login` | 소셜 로그인 시작 (provider: `google`, `kakao`) |
| `POST` | `/api/auth/logout` | 로그아웃 |
| `GET` | `/api/members/me` | 내 정보 조회 |
| `Message` | `/matching/join` | (WebSocket) 챌린지 매칭 참가 |
| `Message` | `/challenge/answer` | (WebSocket) 챌린지 정답 제출 |
| `GET` | `/cmdlee-qs/dashboard/data` | (관리자) 대시보드 통계 데이터 조회 |

<br>
