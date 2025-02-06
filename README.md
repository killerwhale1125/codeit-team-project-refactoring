# 코드잇 팀프로젝트 ( 모읽지 )

### ✅ 프로젝트 개요

#### 모읽지(Moilji) 는 독서 모임과 커뮤니티 기능을 제공하는 플랫폼으로, 사용자들이 함께 책을 읽고 토론하며 성장할 수 있는 환경을 제공합니다.

### ✅ 프로젝트 기간

> 2024.11.15 ~ 2025.01.21

### ✅ 멤버 구성

| 이름  | 역할  | 기능                              |
|-----|-----|---------------------------------|
| 임희원 | FrontEnd | 메인 페이지 UI, 모임 목록 관련 기능( 참여, 공유, 찜, 필터링.. 등 ) |
| 우준석 | FrontEnd  | 로그인 / 회원가입 인증, 검색 페이지, Data Fetching API 구현 |
| 정유진 | FrontEnd  | 마이페이지 UI, 리뷰 관련 기능( 모임, 독서, 좋아요/댓글 ), 무한스크롤 구현 |
| 고수영 | FrontEnd  | 와이어프레임 제작, 디자인 시스템 구성,  디자인 컨셉, UI디자인, 사용자 경험 개선 |
| 정한별 | BackEnd  | 회원가입 / 토큰 기반 로그인, AWS 구축, (모임, 독서) 리뷰, 좋아요, 댓글 기능   |
| 한재민 | BackEnd  | CI/CD 배포 자동화, 모임 관련 기능 |

### ✅ 개발 환경

### Architecture

### API

[API 명세서 바로가기](https://docs.google.com/spreadsheets/d/1lcUy45KENA28HkA6w2CRJwkndWbBS591vnjQbwSXX9s/edit?usp=sharing)

### ✅ 기술 스택

📌 사용 기술

Java, SpringBoot, MySQL, Redis, JPA, Querydsl, Nginx, GitHub Actions, AWS (EC2, RDS, ECR, CodeDeploy, S3), Docker

### ✅ 주요 기여

**아키텍처 개선 (헥사고날 아키텍처 적용)**

문제점<br/>
- Layered 아키텍처 한계 → 비즈니스 로직이 서비스 계층에 집중되어 DB 의존성 증가, 테스트 속도 저하
- 테스트 신뢰성 문제 → Mock Framework 사용으로 코드 복잡성 증가, 낮은 커버리지 및 회귀 버그 발생

개선 사항<br/>
- 비즈니스 로직과 JPA 엔티티 분리 → 도메인 중심 설계(DDD) 적용 가능<br/>
- 서비스 구현체가 추상화된 Input/Output 의존 → 유지보수 및 확장성 향상<br/>
- 테스트에서 DB 의존 제거 → OOP 기반 테스트 적용으로 속도 최적화<br/>

성능 개선 결과<br/>
- 테스트 실행 속도: 1.485초 → 0.114초 (92% 단축, 13배 개선)<br/>

**RDS 병목 및 성능 최적화**

문제점<br/>
- 70만 개 데이터 검색 시 성능 저하 발생
- K6 부하 테스트 (VUser 50, 10분) 결과
  - TPS: 1.2/s
  - Latency: 46.3s
- RDS CPU 사용률 99% → 병목 현상 발생
- ORDER BY 연산 시 Index 미적용으로 성능 저하

개선 사항<br/>
- RDS 모니터링 및 인덱싱 최적화 → CPU 사용률 감소<br/>
- ORDER BY Indexing 적용 → 검색 속도 개선<br/>

성능 개선 결과<br/>
- TPS: 1.2/s → 47.8/s (39.83배 증가)
- Latency: 46.3s → 50ms (926배 단축)
- RDS CPU 사용률 90% 이상 감소


### ✅ 시연 영상
### [시연영상 바로가기](https://youtu.be/MFbDF_wRzKw)
