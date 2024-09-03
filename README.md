# HiBuddy

> 국제학생 커뮤니티 서비스 하이버디 

## Description
국제학생들의 정보 격차를 해소하고 한국어 발음평가를 진행할 수 있습니다. 

스레드에서 게시글, 댓글, 이미지 등을 통해 다양한 정보를 공유하고, 자신의 한국어 발음 능력을 측정할 수 있습니다.

## Demo
### 회원 기능 및 온보딩 
<p float = "left">
<div style="display:flex; flex-wrap:wrap; gap:2px ">
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/c53c61f5-5bd4-469a-8306-9dcc41ac1e2b"/>
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/68c14439-e024-43be-b5a4-6cdf35264eb2"/>
</div>

### 스레드 (Threads)
<div style="display:flex; flex-wrap:wrap; gap:2px ">
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/b2ebf408-c62e-457f-b418-bedd56acf1a8"/>
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/f855a818-79e2-48d2-bf5d-30db572cbebe"/>
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/cdb09410-90c6-4bf6-ba1e-5c7989c1267f"/>
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/6726b432-5c80-459c-ac27-9a2ff25b5392"/>
</div>

### 한국어 발음 테스트
<img width=300 src="https://github.com/Team-HiBuddy/HiBuddy-FE/assets/116625502/914beb18-3e31-44b1-a67d-f6702b4e1390"/>
</p>

## 주요 기능
### 온보딩 기능

### 회원가입 및 로그인 
- JWT & OAuth 2.0 (카카오, 구글) 이용

### 스레드 기능
- 이미지 업로드/수정/삭제 (AWS S3 사용)
- 게시글 생성/수정/삭제
- 댓글 생성/수정/삭제
- 페이징 기능

### 한국어 발음 평가 기능 
- 한국어 음성 인식 후 발음점수 평가 (Levenshtein Distance 알고리즘 & Naver Clova Speech API 사용), wav 파일 전송 및 기록 
- 난이도별 스크립트 선택 기능
- 발음 & Pitch 점수 확인  

### 기타 기능
- 마이페이지
- 댓글, 게시글 좋아요 기능
- 댓글, 게시글 스크랩 생성/해제 

## Stack
- **Language** : Java
- **Library & Framework** : SpringBoot
- **Database** : AWS RDS (MySQL)
- **ORM** : JPA
- **Deploy** : AWS EC2, Nginx 


## Server Architecture
![image](https://github.com/user-attachments/assets/8a47d2e9-9645-476f-a3a1-16d723d029ec)

## Role & Contribution

**Backend**
- 전체 아키텍처 구성
- 스레드 API 개발 (게시글, 댓글, 스크랩, 좋아요 etc)
- 한국어 발음평가 API 개발 (Naver Clova Speech API 연동)

**Devops**
- AWS EC2, VPC, RDS 설정
- AWS S3 설정
- Nginx를 이용한 EC2 무중단 배포 수행

**etc**
- 프로젝트 기획 및 개발 일정 및 이슈 관리 

## Developer
* **이수용** ([leesuyong4029](https://github.com/leesuyong4029)) 
* **이강산** ([guridaek](https://github.com/guridaek)) 
* **오영수** ([YoungSuOh](https://github.com/YoungSuOh))
