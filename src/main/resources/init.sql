INSERT INTO category (title, icon, description)
VALUES ('IT 및 컴퓨터', '💻', '정보처리기사, 리눅스마스터 등 IT 관련 시험 문제'),
       ('산업기술/기능사', '⚙️', '전기기사, 기계정비, 산업안전 등 기술 자격 및 기능사'),
       ('공무원 및 국가시험', '🏛️', '9급·7급 공무원, 경찰·소방, 국가직 시험 대비'),
       ('금융/회계', '💰', '회계사, 세무사, 은행 필기, 펀드·보험 관련 자격'),
       ('어학/자격', '🌐', '토익, 토플, JLPT, HSK, 한국어능력시험 등'),
       ('의학/간호/보건', '🩺', '간호사 국가고시, 응급구조사, 보건직 공무원 등'),
       ('교육/임용', '📚', '교원임용고시, 교육학, 유아·특수교육 등'),
       ('유머/밈/기타', '😆', '가볍게 즐기는 밈/유머 문제, 쉬는 타임 콘텐츠');


INSERT INTO ai_prompts (name, template, description, created_at, updated_at)
VALUES ('quiz_generation',
        $$
너는 {{TOPIC}}  시험문제 전문가야
다음 정보를 바탕으로 JSON 형식의 퀴즈 데이터를 만들어줘.
---
주제: {{TOPIC}}
설명: {{DESCRIPTION}}
문제 수: {{COUNT}}개
난이도: {{DIFFICULTY}} (easy, medium, hard)
문제 유형: {{QUESTIONTYPE}} (multiple, shorts)
규칙 요약:
	•	반드시 JSON 배열로 출력할 것 ([])
	•	항목 수는 정확히 {{COUNT}}개
	•	type: {{QUESTIONTYPE}}
	•	question: 질문 텍스트
	•	options: 객관식 보기 (multiple일 때만, 4개 필수)
	•	correctAnswer: 정답 인덱스 (1~4), 주관식은 null, 단일 정답으로만
	•	correctAnswerText: 정답 텍스트 (multiple일 때만 필수)
	•	explanation: 문제에 정답에 대한 해설
---

출력 형식은 다음과 같은 JSON 배열로 해줘 예시 :

```json
[
  {
    "type": "multiple",
    "question": "다음 Java 코드를 실행하면 출력 결과는?",
    "options": ["6", "7", "8", "9"],
    "correctAnswer": [3],
    "correctAnswerText": "8",
    "explanation": "5 + 3은 8이므로, 정답은 3번이다."
  },
  {
    "type": "shorts",
    "question": "운영체제에서 실행 중인 프로그램을 무엇이라고 하나요?",
    "options": [],
    "correctAnswer": [],
    "correctAnswerText": "프로세스",
    "explanation": "실행 중인 프로그램은 프로세스라 부른다."
  }
]
---
지시사항:
위 예시는 이해를 돕기위한 문제이며 활용 금지 지금부터 {{TOPIC}} 주제에 대해 {{DESCRIPTION}}을 반영한 {{DIFFICULTY}} 난이도의  {{QUESTIONTYPE}} 타입 문제를 총 {{COUNT}}개 생성해줘.
출력은 반드시 위에 설명한 JSON 배열 형식으로만 해.
그 외 어떤 문장도 출력하지 마.
---
$$,
        '기본 퀴즈 출제 프롬프트 (객관식/주관식 지원)',
        NOW(), NOW());
