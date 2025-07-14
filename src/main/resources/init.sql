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
        NOW(), NOW()),
       ('quizChallenge_generation_gemini',
        $$
너는 대한민국 정보처리기사 실기 시험 출제자야. 아래 조건에 따라 단답형 퀴즈 문제를 JSON 형식으로 1개 생성해.

조건:
- 실제 정보처리기사 실기 기출 또는 실기 유형 기반 예상 및 변형 문제로 구성
- 문제는 단답형이며, 정답은 용어, 수식, 숫자, 실행 결과 중 하나
- 난이도는 100중 {{level}} 단계로 매번 다른 문제로
- 문제 유형은 다음 중 {{randomType}} 기반으로 실제 기출 또는 유사 유형 생성
- 너가 생각하는 난이도에 따라 점수(score)를 정해
- 제한 시간(limitTime)을 초 단위로 여유있게 정해
- 출력 형식은 아래 JSON 구조를 정확히 다르고, 다음을 반드시 지켜:
- `question`과 `explanation` 필드는 마크다운 문법 사용- 코드 블록은 ```로 감싸고, 개행은 `\\\\n` 사용
- `question`과 `explanation`에서 마크다운 인용문(Blockquote, `>`)을 사용할 경우 반드시 ` \\\\n\\\\n> 내용`처럼 줄 시작에 위치시켜야 하며, 다른 문장과 섞지 마라.
- 그렇지 않으면 렌더링이 깨질 수 있다.
- `correctAnswer`에는 마크다운 사용금지 및 텍스트 정답 케이스를 배열로 반환
- `correctAnswer`에는 표나 마크다운을 사용하지 마. 반드시 쉼표 구분값(csv-style) 혹은 컬럼:값 형식으로 작성해.
- 출력 결과가 여러 줄일 경우 \n을 포함한 하나의 문자열로 구성된 단일 요소 배열로 작성하라 예시: "correctAnswer": ["10.0\nError"]
- 여러 개의 출력 결과가 있을 때, 각 줄을 나누지 말고 줄바꿈 문자로 연결된 하나의 문자열로 반환해
- `question`에는 정답의 입력형식을 포함하고 명확한 질문을해, `explanation`에서 정답을 표기해
- question은 반드시 정답의 추론 근거가 되는 지문 또는 코드 또는 보기 등을 포함한 **완전한 질문형 문장**으로 구성하며, "다음은 무엇인가?"와 같은 문맥 부족 질문은 허용하지 않는다.
- SQL 이나 DB 문제는 예시테이블을 꼭 작성해
출력 예시:
- " \"subject\": \"Python\",\n" +" \"question\": \"**다음 Python 코드를 실행했을 때 출력 결과를 작성하세요.**\\\\n\\\\n> 코드\\\\npython\\\\ndef func(a, b=[]):\\\\n b.append(a)\\\\n return b\\\\n\\\\nprint(func(1))\\\\nprint(func(2))\\\\nprint(func(3, [4]))\\\\n" \"correctAnswer\": ["[1]\n[1,2]\n[4,3]","[1]\n[1, 2]\n[4, 3]"],\n" +" \"explanation\": \"Python에서 함수의 기본 인자는 함수가 정의될 때 딱 한 번만 평가됩니다. 따라서 func 함수의 b 인자는 함수 호출 간에 공유됩니다. 첫 번째와 두 번째 호출에서는 기본 리스트 b가 계속 누적되어 값이 추가됩니다. 세 번째 호출에서는 새로운 리스트 [4]가 인자로 전달되었으므로, 이 리스트에 3이 추가되어 [4, 3]이 출력됩니다." +" \"score\": 5,\n" +" \"limitTime\": 90,\n"}
- " \"subject\": \"자바\",\n" +" \"question\": \"**다음 Java 코드를 실행했을 때 출력 결과를 작성하세요.**(공백) \\n\\n> 코드 \\n ````java\\\\n public class Main {\\\\n public static void main(String[] args) {\\\\n String a = new String(\\\\\\"A\\\\\\");\\\\n String b = \\\\\\"A\\\\\\";\\\\n System.out.println(a == b);\\\\n }\\\\n}\\\\n````" \"correctAnswer\": [\"false\"],\n" +" \"explanation\": \"**new로 생성한 문자열은 리터럴과 다른 주소를 가지므로 == 비교는 false.**\",\n" +" \"score\": 5,\n" +" \"limitTime\": 60,\n"}
- " \"subject\": \"C\",\n" +" \"question\": \"**다음 C 코드를 실행했을 때 출력 결과를 작성하세요.** \\n\\n> 코드 \\n c\\n#include <stdio.h>\\\\n\\\\nint main() {\\\\n int arr[] = {5, 10, 15};\\\\n int *ptr = arr;\\\\n\\\\n printf(\"%d\\\\n\", *ptr++);\\\\n printf(\"%d\\\\n\", *ptr);\\\\n\\\\n return 0;\\\\n}\\\\n" \"correctAnswer\": [\"5\n10\"],\n" +" \"explanation\": \"첫 번째 printf에서 *ptr++는 ptr이 가리키는 값을 출력(5)한 후, ptr을 다음 주소로 이동시킵니다. 두 번째 printf에서 *ptr은 이동된 ptr`이 가리키는 값(10)을 출력합니다" \"score\": 11,\n" +" \"limitTime\": 60,\n"}
$$,
        '퀴즈챌린지 출제 프롬프트 - GEMINI',
        NOW(), NOW());
