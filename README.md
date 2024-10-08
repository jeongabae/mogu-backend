# 📦 매지리민들을 위한 공구 매칭 서비스, 모구 📦
![image](https://github.com/user-attachments/assets/a3b02fe5-a663-4bfd-9b0a-afbb43cc66df)



## 모든 연세인을 위한 공구, 모구
매지리에서 만족스러운 공동구매를 하려는 연세대학교 미래캠퍼스 주변 자취생 및 기숙사생에게 원하는 시간에 원하는 양의 물품을 가져갈 수 있도록 공구 의사가 맞는 학생들을 연결하여 중개해주는 앱

## 팀 카페이너
💫 Member
- 🙋🏻‍♀️ 손혜림 PM | 문제정의 및 리서치, 페르소나 설정, 플로우 차트 설계, 일정 관리 및 회의 진행, 전반적인 프로세스 관리
- 🙋🏻‍♀️ 남윤지 PD | 와이어프레임 구축, 디자인 스토리보드 제작, 전반적인 컨셉과 디자인 스타일 설정
- 🙋🏻‍♀️ 김민경 FE | UI 구현, 사용자 인터랙션 구현, 기능 구현, API 연동
- 🙋🏻‍♂️ 배진우 FE | UI 구현, 사용자 인터랙션 구현, 기능 구현, API 연동
- 🙋🏻‍♀️ 전가배 BE | 로직, ERD 설계, 데이터베이스 관리, 인증 및 서버, API 설계

## 내가 담당한 부분 : 기획 및 백엔드
### 구현 기능
- 공구 관련 기능
  - 공구 등록
  - 공구 목록 조회, 공구 상세 조회, 공구 목록 필터링 조회, 공구 검색(기본, 마감 임박일 가까운 순, 잔여 수량 많은 순)
  - 공구 오픈채팅방 링크 조회
  - 내가 진행한 공구 목록 조회, 내가 진행 중인 공구 목록 조회
  - 공구 수정
  - 공구 상태 변경
  - 공구 삭제
  - 매일 자정(0시)에 공구 마감일 여부 확인하여 마감일이면 공구 마감으로 공구 상태 변경(스케줄러 이용)
- 공구 참여 관련 기능
  - 공구 참여
  - 특정 공구의 공구 참여자 정보 조회
  - 특정 공구의 내 공구 참여 여부 조회
  - 내가 참여한 공구 목록 조회, 내가 참여 중인 공구 목록 조회
  - 특정 공구 참여 삭제
- 찜 관련 기능
  - 찜 추가
  - 특정 공구에 대한 유저 찜 확인 여부 조회
  - 특정 공구 찜 유저 아이디 조회
  - 찜 목록 조회, 기한 임박 순 찜 목록 조회, 잔여 수량 기준 찜 목록 조회
  - 찜 삭제
- 공구 이미지 관련
  - 공구 이미지 로드
- 회원 가입 및 로그인 기능
  - 회원 닉네임 중복 체크, 회원 전화번호 중복 체크
  - 메일 인증코드 발급(회원가입 시), 메일 인증코드 확인(회원가입 시)
  - 회원 가입
  - 이메일 DB 존재 유무 조회
  - 임시 비밀번호 발급
  - 로그인
- 멤버 관련 기능
  - 회원 정보 조회
  - 회원 닉네임 수정, 비밀번호 수정, 전화번호 수정
  - 회원 비밀번호 일치 여부 확인(for 정보 수정)
  - 회원 탈퇴
 
    
## 소통 채널
![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
![KakaoTalk](https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=000000)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

## 배경
- 학교 주변에 거주하는 자취생과 기숙사생은 주변에 오프라인 매장이 없어 물품을 구매하는 데 불편함을 겪음. 배송시킬 때도 더 이득이 되는 방향으로 구매하려면 대량으로 구매해야 하며 결국 비용적인 측면에서 부담감을 느낌
- 물품 배송을 시키면서 소비와 어쩔 수 없는 매지리 특성에 불편함을 느낌
- 물품을 수령받은 이후 물품 대량 물품을 보관할 기간이나 공간이 충분하지 않아 보관/ 추후의 처리 측면에서도 문제 발생 가능

## 기획
#### 1. 인터뷰
  
  (1) 저렴하게 구매하기 위해 대량으로 사야 했음.

  (2) 기숙사의 경우, 공간 문제로 대량 구매가 힘들었음. 대량으로 구매한다 해도 냉장고의 공간 부족 문제로 이어짐 

  (3) 유통기한 긴 상품을 주로 구매하는 경향. 빨리 먹어야 하는 상품은 소량으로 구매하기 어려워 기한 넘기면 자주 버리게 됨

  (4) 요리 재료를 대량으로 구매했다가 음식물쓰레기 양만 많아져 '불필요한 처리'라는 문제점 유발하기도 함

  (4) 갑작스럼게 생필품을 구매하려면 자취방으로부터 꽤 떨어진 편의점에 가야했기에 불편했음
#### 2. 타겟 및 목표
  🧑‍🤝‍🧑학교 주변에서 상품(생필품, 음식)을 배송시키는 자취생 & 기숙사생, 저렴하게 필요한 만큼의 상품을 가지고 싶어하는 학생들
  
  📍‘모구’를 통해 학교 주변 자취생&기숙사생들이 원활하게 공동구매할 수 있도록 연결해주기

#### 3. 메인 기능
  - 검색 : 관심있는 진행중인 공구 찾아보기
  - 참여 : 공구 참여하기
  - 모집 : 공구 글 직접 등록하여 함께 공구할 참여자 모집하기
  - 참여자 관리 : 주최자가 단계별로 공구를 진행하는 과정이 용이하도록 참여자들의 참여수량 및 참여자 정보 리스트업하여 보여주기
  - 찜 : 현재 진행중인 공동구매 중 관심있는 상품을 관심공구로 등록


![image](https://github.com/user-attachments/assets/a540e2d0-b426-44a4-8eac-1853fc4b5b3f)

#### 3.1 스웨거
<img width="1086" alt="image" src="https://github.com/user-attachments/assets/056b40fe-3c49-4fb6-aa52-d1087dfd8926">
<img width="1085" alt="image" src="https://github.com/user-attachments/assets/7f90171a-79fd-4180-851f-a55be9a006b5">
<img width="1085" alt="image" src="https://github.com/user-attachments/assets/3568b21d-076e-4518-b7f1-a05bf7ecfe78">
<img width="1086" alt="image" src="https://github.com/user-attachments/assets/66bda9ce-11c4-4ec0-b993-a064ac0caeea">
<img width="1085" alt="image" src="https://github.com/user-attachments/assets/23728433-2311-4d83-802c-4f2c947684be">


#### 4. 플로우 차트
![image](https://github.com/user-attachments/assets/b9c4b410-f947-4731-805b-262310533d74)

#### 5. 와이어 프레임
![image](https://github.com/user-attachments/assets/01fd857c-2bed-4174-b7e8-d384f1fd2966)

## 💻 아키텍쳐
![image](https://github.com/user-attachments/assets/e465a234-cd6b-4c73-9d11-3704384a4a73)

## 🛠 기술 스택
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![React Native](https://img.shields.io/badge/react_native-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Springboot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

- Language : Java, Java Script
- Frontend : React Native
- Backend : Spring Boot
- Infrastructure : AWS EC2
- Database : AWS RDS(Mysql)

## 시연 영상
https://youtu.be/FJaiCGogSds

## 프로젝트를 진행하면서 생긴 문제점 및 해결방안
- 개발 과정에서 사소한 에러 문제
- github에서 파일을 합치는 과정에서의 충돌
- 큰 문제점 X


## 프로젝트 성공과 실패 혹은 아쉬운 부분 / 개선사항
</details>

![image](https://github.com/user-attachments/assets/6fa3b087-8946-4078-bc7b-ff96dc35fbaf)

## 📞Contact
Email :gabea52@naver.com

## FrontEnd github
https://github.com/YonseiDOIT/Mogu
  
