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
| 한재민 | BackEnd  | CI/CD 배포 자동화, AWS 구축, 모임 및 챌린지 관련 기능, 아키텍쳐 개선, 알라딘 도서 정보 크롤링 |

### ✅ 서버 구성도

![image](https://github.com/user-attachments/assets/3bab8b70-61e8-4857-b510-3a89c49f2981)

### ✅ 기술 스택

- Java, SpringBoot, MySQL, Redis, JPA, Querydsl, Nginx, GitHub Actions, AWS (EC2, RDS, ECR, CodeDeploy, S3), Docker

### ✅ 프로젝트 개선 사항 정리 블로그
- [CPU, Memory 모니터링을 통한 성능 측정 및 병목현상 원인 파악과 해결 과정](https://killerwhale1125.github.io/posts/%EB%B3%91%EB%AA%A9%ED%98%84%EC%83%81-%EC%9B%90%EC%9D%B8-%ED%8C%8C%EC%95%85%EA%B3%BC-%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81-%EB%B0%8F-%ED%95%B4%EA%B2%B0-%EA%B3%BC%EC%A0%95/)
- [JPA Test 문제점과 헥사고날 아키텍쳐 개선 및 장단점](https://killerwhale1125.github.io/posts/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%A3%BC%EB%8F%84-%EA%B0%9C%EB%B0%9C%EC%97%90%EC%84%9C-Layered-Architecture%EC%9D%98-%ED%95%9C%EA%B3%84%EC%99%80-%EA%B0%9C%EC%84%A0-%EC%A0%84%EB%9E%B5/)

### ✅ 프로젝트 개선 사항 및 트러블 슈팅

**1. 아키텍처 개선 (헥사고날 아키텍처 적용)**

**문제점**<br/>
- Layered 아키텍처 한계 → 비즈니스 로직이 서비스 계층에 집중되어 DB 의존성 증가, 테스트 속도 저하
- 테스트 신뢰성 문제 → Mock Framework 사용으로 코드 복잡성 증가, 낮은 커버리지 및 회귀 버그 발생

<br/>

**개선 사항**<br/>

![image](https://github.com/user-attachments/assets/354eb62c-b228-49c1-8199-7dd173207f06)

![image](https://github.com/user-attachments/assets/a9c8a997-9bb2-4df2-a60a-75d5b6ebe8df)

- 비즈니스 로직과 JPA 엔티티 분리 → 도메인 중심 설계(DDD) 적용 가능<br/>
- 서비스 구현체가 추상화된 Input/Output 의존 → 유지보수 및 확장성 향상<br/>
- 테스트에서 DB 의존 제거 → OOP 기반 테스트 적용으로 속도 최적화<br/>

<br/>

**2. RDS 병목 및 성능 최적화**

![image](https://github.com/user-attachments/assets/a63b2bef-9501-4661-a7b2-05d7ca7454a4)

**문제점**<br/>
- 70만 개 데이터 검색 시 성능 저하 발생
- K6 부하 테스트 (VUser 50, 10분) 결과
  - TPS: 1.2/s
  - Latency: 46.3s
- RDS CPU 사용률 99% → 병목 현상 발생 및 응답 대기로 인한 DB 커넥션 사용 불가
- ORDER BY 연산 시 Index 미적용으로 성능 저하

<br/>

**개선 사항**<br/>
- RDS 모니터링 및 인덱싱 최적화 → CPU 사용률 감소<br/>
- ORDER BY Indexing 적용 → 검색 속도 개선<br/>

<br/>

**성능 개선 결과**<br/>

![image](https://github.com/user-attachments/assets/6b81abcb-8f6e-4b87-a47b-62571a66af4e)

- TPS: 1.2/s → 47.8/s (39.83배 증가)
- Latency: 46.3s → 50ms (926배 단축)
- RDS CPU 사용률 90% 이상 감소


### ✅ 시연 영상
### [시연영상 바로가기](https://youtu.be/MFbDF_wRzKw)
