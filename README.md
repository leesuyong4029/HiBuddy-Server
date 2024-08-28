# HiBuddy

> 국제학생 커뮤니티 서비스 하이버디 

## 📖 Description
국제학생들의 정보 격차를 해소하고 한국어 발음평가를 진행할 수 있습니다. 

스레드에서 게시글, 댓글, 이미지 등을 통해 다양한 정보를 공유하고, 자신의 한국어 발음 능력을 측정할 수 있습니다! 

## :baby_chick: Demo
<p float = "left">
  <img src = "https://github.com/user-attachments/assets/8c2f9ddb-3382-4f04-8f6e-065c687e9472" width = 200 />

## ⭐ Main Feature
### 온보딩 기능

### 회원가입 및 로그인 
- JWT & OAuth 2.0 (카카오, 구글) 이용

### 스레드 기능
- 이미지 업로드/수정/삭제 (AWS S3 사용)
- 게시글 생성/수정/삭제
- 댓글 생성/수정/삭제
- 페이징 기능

### 한국어 발음 평가 기능 
- 한국어 음성 인식 후 발음점수 평가 (Levenshtein Distance 알고리즘 & Naver Clova Speech API 사용)
- 스크립트 선택 기능 

### 기타 기능
- 마이페이지
- 댓글, 게시글 좋아요 기능
- 댓글, 게시글 스크랩 생성/해제 

## 🔧 Stack
- **Language** : Java
- **Library & Framework** : SpringBoot
- **Database** : AWS RDS (MySQL)
- **ORM** : JPA
- **Deploy"" : AWS EC2, Nginx 


## 🔨 Server Architecture

## ⚒ CI/CD

## 👨‍💻 Role & Contribution

**Backend**
- 전체 아키텍처 구성
- 스레드 API 개발 (게시글, 댓글, 스크랩, 좋아요 etc)
- 한국어 발음평가 API 개발 (Naver Clova Speech API 연동)

**Devops**
- AWS Cloud 9을 통한 EC2, VPC, RDS 설정
- AWS S3 설정
- Nginx를 이용한 EC2 무중단 배포 수행

**etc**
- 프로젝트 기획 및 개발 일정 및 이슈 관리 

## 👨‍👩‍👧‍👦 Developer
* **이수용**
* **이강산**
* **오영수**
