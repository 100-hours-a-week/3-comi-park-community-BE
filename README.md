# Gossip Girl (BackEnd)

> 가십거리 등 다양한 이야기를 가볍게 나누는 커뮤니티입니다.

<img width="1528" height="1080" alt="image" src="https://github.com/user-attachments/assets/0e12c62a-ce01-41d6-a87e-9ee23d516f07" />

<br>
<br>
<br>

## 목차

- [소개](#소개)
    - [시연 영상](#시연-영상)
- [개발 정보](#개발-정보)
- [데이터베이스](#데이터베이스)
    - [게시글 통계 정보 테이블](#게시글-통계-정보-테이블)
    - [이미지 테이블](#이미지-테이블)
    - [좋아요 테이블](#좋아요-테이블)
- [클라우드 아키텍처](#클라우드-아키텍처)
    - [요약](#요약)
    - [상세 설계](#상세-설계)
- [도큐먼트](#도큐먼트)

<br>
<br>
<br>

## 소개

- 미국 드라마 'Gossip Girl'에 영감받았어요
- 가십거리뿐만 아니라 다양한 주제로 이야기를 나눌 수 있는 커뮤니티에요

<br>

### 시연 영상

> https://youtu.be/DljeHMnJPa8 유튜브에서 고화질로 확인할 수 있습니다

https://github.com/user-attachments/assets/54b18e8c-7624-4d55-98f8-522400c61271



- 회원가입을 해야만 이용할 수 있습니다
- 회원은 게시글당 1장 이하의 이미지를 첨부할 수 있습니다
- 회원은 본인이 작성한 게시글과 댓글의 자유롭게 수정/삭제할 수 있습니다

<br>
<br>
<br>

## 개발 정보

- 개발 기간: 2025.10 - 2025.12 (2개월)
- 개발 인원: 1인 (개인 프로젝트)
- 개발 스택:
    - SpringBoot
    - MySQL
- 프론트엔드 GitHub 레포지터리: https://github.com/100-hours-a-week/3-comi-park-community-FE

<br>
<br>
<br>

## 데이터베이스

<img width="1427" height="770" alt="image" src="https://github.com/user-attachments/assets/4f8456e0-2aa9-432a-8b08-3fb7bf2f83bc" />

<br>

### 게시글 통계 정보 테이블

- `게시글의 조회수, 좋아요수, 댓글 수의 관리 주체를 인메모리 데이터베이스(이하 DB) 대신 RDB에 둔 이유`
    - 우선 게시글 통계 정보 테이블을 별도로 관리하는 이유는 게시글 조회 요청마다 좋아요 테이블과 댓글 테이블에서 COUNT 연산하지 않기 위함
        - 즉, 단순 캐싱용 데이터
    - 만약 통계 정보 테이블을 인메모리DB에 둔다면 인메모리DB의 빠른 속도가 퇴색됨
        - 게시글 조회 시 RDB의 게시글 정보와 인메모리DB의 통계정보가 하나의 응답으로 함께 반환
        - 즉, 인메모리DB에서 빠르게 통계 정보를 조회해도 결국 RDB의 속도에 맞춰 클라이언트에게 전달되기 때문
    - 따라서 RDB 속도에 묶인다면 굳이 통계 정보를 인메모리DB에서 관리할 이유가 없다고 판단
        - RDB에서 통계 정보 테이블을 두고 COUNT 대신 댓글/좋아요 생성/삭제 및 조회할 때마다 값을 increment/decrement하는 방식으로 결정

<br>

### 이미지 테이블

- `이미지 테이블 별도 존재 vs 게시글/회원 테이블 내 이미지 컬럼`
    - 현재는 게시글당 1장 이하의 이미지 첨부 가능하기 때문에 게시글/회원 테이블에 이미지 컬럼 관련 컬럼(filename, object_key, url)을 추가할 수도 있었음
    - 그러나 인스타그램이 업로드 가능한 사진 개수를 점점 늘려왔듯, 서비스가 지속될수록 여러 이미지를 첨부하길 원하는 사용자의 요청을 염두에 두고 확장 가능한 이미지 테이블을 별도로 구성

<br>

### 좋아요 테이블

- `복합키를 기본키로 설정 이유`
    - 좋아요는 게시글과 회원이 있어야만 생성
    - 회원의 특정 게시글 좋아요 여부, 게시글의 좋아요를 누른 회원 카운트에만 사용되기 때문에 의미 없는 id (auto_increment)를 기본키로 설정할 필요가 없다고 판단
    - 추가로 한 회원은 한 게시글에 좋아요를 1번만 누를 수 있다는 조건을 쉽게 만족 가능

 <br>

- `(post_id, member_id) 복합키 순서`
    - (post_id, member_id) 순으로 설정 시
        - post_id & member_id 검색에 활용
        - post_id 검색에 활용
        - member_id 검색에 활용 X
    - 게시글에 사용자가 좋아요를 눌렀는지 안 눌렀는지 확인할 때 (post_id, member_id) 활용
    - member_id를 먼저 기준으로 두고 연산할 때가 없어 (post_id, memebr_id)순으로 복합키 설정
        - 다만 나중에 ‘내가 좋아요 누른 게시글 목록’ 보기 기능이 추가된다면 (member_id, post_id) 인덱스 추가 필요

<br>
<br>
<br>

## 클라우드 아키텍처

> 목표: 100만 MAU까지 확장 가능한 구조

<img width="1960" height="1010" alt="comi park(박경미)-gossip-girl-아키텍처" src="https://github.com/user-attachments/assets/bb89a594-cdb4-4b5d-8a02-643afe138b5f" />

<br>

### 요약

- 프론트엔드: S3 + CloudFront로 배포
- 백엔드: ELB → Auto Scaling Group → EC2 내 스프링 도커 컨테이너로 배포
- 멀티파트 이미지 업로드: API Gateway → Lambda → S3 저장

<br>

### 상세 설계

- `도커 이미지 레지스트리 ⇒ 개발 시엔 Portainer Private Registry, 배포 시엔 ECR`
    - 원하는 조건
        - Private 레지스트리
        - NAT 없이 Private Subnet EC2에서 레지스트리에 접근 가능
    - Docker Hub 제외
        - Private 레지스트리 1개 생성 가능
        - 그러나 외부 레지스트리이기 때문에 Private Subnet EC2에서 접근하려면 인터넷 필요 (비용 발생)
    - Portainer Private Registry
        - EBS 허용하는 범위까지 도커 이미지 저장 가능
        - Public Subnet EC2의 사설IP 통해 Private Subnet EC2에서 접근 가능
            ```bash
            docker pull [Public Subnet EC2 사설IP]:5000/image-name # 가능
            ```
    - 최종 배포 시 ECR 사용
        - 개발 단계에서 비용을 고려해 Portainer와 Registry를 새 인스턴스에 띄우는 대신 Nginx만 존재하는 Public Subnet EC2에 띄웠지만, 원래 이 둘은 Private Subnet에 속하는 게 더 보안적임
        - 배포를 위해 보안을 신경 써 별도의 Private Subnet EC2를 생성해 이 둘을 띄운다면, 그 비용으로 ECR 도입하는 게 이득이라고 봄
        - 왜냐하면
            - 한달 동안 EC2 띄우는 비용보다 ECR 프리티어 범위 내에서 사용하는 게 더 저렴함
                - 1년 간 Private Repository 월 500MB 이하 도커 이미지 저장 무료
                - 동일 리전 내 다른 AWS 서비스(EC2 등) 데이터 송수신 무료
            - 내가 직접 관리하는 Portainer Registry보다 AWS 관리형 서비스인 ECR의 우수한 보안
            - 100만 MAU까지 구조를 확장해나갈 때 ECS 도입 시 확장성 좋음
        - 따라서 개발 단계에선 Portainer Registry, 배포 단계에선 ECR 사용

<br>

- `API Gateway + Lambda로 사용자가 업로드한 이미지는 S3에 저장`
    - 이미지 저장 위치 변경
        - 개발 단계에선 멀티파트 업로드한 이미지를 서버의 폴더에 저장 후 정적 서빙
        - 배포 시엔 멀티파트 업로드한 이미지를 S3에 저장 후 CloudFront로 서빙하도록 변경
        - 변경 이유는 아래 자세히 나옴
    - 변경 이유
        - 기존대로 이미지 파일을 서버에 저장 시 EFS 사용 흐름이 자연스럽다고 생각
            > 아래 로직과 문제점은 프론트엔드를 CloudFront + S3로 서빙하기로 변경하기 전, EC2로 WS를 띄운다는 설계 하에 있음
            1. 클라이언트가 `/api/images/**` 요청 시 API Gateway가 Lambda로 전달
            2. Lambda가 EFS에 이미지 저장
                - 여러 EC2 인스턴스가 이미지에 접근해야 하기 때문에 EFS 사용
            3. Lambda가 클라이언트에 이미지 저장 경로 응답
        - EFS 사용 시 문제점
            - EFS는 파일 시스템 레벨의 I/O라서 S3보다 비쌈
            - Stateless한 Lambda는 켜질 때마다 EFS 마운트해야 함
                - Stateless한 Lambda를 Stateful하게 사용
                - 콜드 스타트에 마운트까지 추가되는 것임
            - EC2(웹 서버)가 EFS 마운트해서 정적 서빙
                - EC2 ↔ EFS 간 네트워크 트래픽 비용 발생
                    - 서로 다른 AZ에 있으면 더 비싼 요금
                - EC2 → 인터넷 아웃바운드 트래픽 비용
                    - 모든 요청이 EC2를 거쳐 EC2 자원 사용함
        - S3 도입 시 장점
            - 파일 시스템인 EFS보다 저렴한 S3 저장 및 I/O 비용
            - 캐시 덕분에 CloudFront에 캐싱된 이미지에 접근하면 S3까지 가지 않아 S3 요청 수 절감
            - 이미지 요청 처리와 EC2가 분리되어 아무리 많은 이미지 요청이 들어와도 EC2에 부하 X
                > 프론트엔드를 EC2로 서빙한다는 가정
    - S3 + CloudFront 사용 로직
        1. 클라이언트가 이미지 업로드 멀티파트 요청
        2. API Gateway가 Lambda로 전달
        3. Lambda에서 S3에 이미지 업로드
        4. Lambda가 클라이언트에 CloudFront URL과 ObjectKey 응답
            > 추후 최초 이미지 업로드 시 `/pending` prefix로 이미지 URL 생성
            > 게시글/회원 수정/생성 요청 시 `/pending` → `/active`로 이동하는 로직 추가 예정

<br>
<br>
<br>

## 도큐먼트

> 개발 및 설계 시 고민거리, 트러블슈팅 등의 Wiki가 추가될 예정입니다
