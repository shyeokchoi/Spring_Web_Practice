# Spring_Web_Practice
회원가입, 인증, 게시글 작성, 댓글 작성, 파일 업로드/다운로드, 게시글 임시저장 등을 지원하는 간단한 게시판 프로젝트입니다.
## 목적
1. Spring Boot 프레임워크 사용법을 익힙니다.
2. 다양한 케이스에 대해 적절한 예외 처리를 하는 연습을 합니다.
3. 단순히 글만 등록하는 게시판이 아닌, 파일을 등록하고 게시글을 임시저장 하는 등 복잡도가 올라간 게시판 앱을 개발하며 시행착오를 경험합니다.
4. Service Layer에 대한 기초적인 unit test를 작성해봅니다.

## 사용된 기술

![Spring Boot](https://img.shields.io/badge/Spring_boot-blue) ![MyBatis](https://img.shields.io/badge/MyBatis-orange) ![MySql](https://img.shields.io/badge/MySql-green)

## DB 테이블 정보
```mysql
-- web_study.comment definition

CREATE TABLE `comment` (
  `no` int NOT NULL AUTO_INCREMENT,
  `post_no` int NOT NULL,
  `created_at` bigint NOT NULL,
  `deleted_at` bigint DEFAULT NULL,
  `author_no` int NOT NULL,
  `content` varchar(300) NOT NULL,
  `modified_at` bigint DEFAULT NULL,
  `modifier_no` int DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `comment_to_post` (`post_no`),
  KEY `comment_to_author` (`author_no`),
  KEY `comment_to_modifier` (`modifier_no`),
  CONSTRAINT `comment_to_author` FOREIGN KEY (`author_no`) REFERENCES `member` (`no`),
  CONSTRAINT `comment_to_modifier` FOREIGN KEY (`modifier_no`) REFERENCES `member` (`no`),
  CONSTRAINT `comment_to_post` FOREIGN KEY (`post_no`) REFERENCES `post` (`no`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- web_study.file_info definition

CREATE TABLE `file_info` (
  `no` int NOT NULL AUTO_INCREMENT,
  `origin_name` varchar(100) NOT NULL,
  `save_name` varchar(100) NOT NULL,
  `save_path` varchar(100) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `parent_no` int DEFAULT NULL,
  `created_at` bigint DEFAULT NULL,
  `author_no` int DEFAULT NULL,
  `modified_at` bigint DEFAULT NULL,
  `modifier_no` int DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `parent_type` varchar(100) DEFAULT NULL,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `file_info_UN` (`save_name`),
  KEY `file_to_post` (`parent_no`),
  KEY `file_to_author` (`author_no`),
  KEY `file_to_modifier` (`modifier_no`),
  KEY `file_info_save_name_IDX` (`save_name`) USING BTREE,
  CONSTRAINT `file_to_author` FOREIGN KEY (`author_no`) REFERENCES `member` (`no`),
  CONSTRAINT `file_to_modifier` FOREIGN KEY (`modifier_no`) REFERENCES `member` (`no`),
  CONSTRAINT `file_to_post` FOREIGN KEY (`parent_no`) REFERENCES `post` (`no`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- web_study.`member` definition

CREATE TABLE `member` (
  `no` int NOT NULL AUTO_INCREMENT,
  `phone` varchar(15) NOT NULL,
  `withdrawn_at` bigint DEFAULT NULL,
  `pw` varchar(100) NOT NULL,
  `id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `member_unique_id` (`id`),
  UNIQUE KEY `member_unique_email` (`email`),
  KEY `member_id_IDX` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3;

-- web_study.member_auth definition

CREATE TABLE `member_auth` (
  `no` int NOT NULL AUTO_INCREMENT,
  `member_no` int NOT NULL,
  `access_token` varchar(300) NOT NULL,
  `ip_addr` varchar(100) DEFAULT NULL,
  `user_agent` varchar(300) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `created_at` bigint DEFAULT NULL,
  `expired_at` bigint DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `member_auth_FK` (`member_no`),
  KEY `member_auth_access_token_IDX` (`access_token`) USING BTREE,
  CONSTRAINT `member_auth_FK` FOREIGN KEY (`member_no`) REFERENCES `member` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- web_study.post definition

CREATE TABLE `post` (
  `no` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `created_at` bigint NOT NULL,
  `modified_at` bigint DEFAULT NULL,
  `deleted_at` bigint DEFAULT NULL,
  `author_no` int NOT NULL,
  `modifier_no` int DEFAULT NULL,
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`no`),
  KEY `modifier_no` (`modifier_no`),
  KEY `author_no` (`author_no`),
  CONSTRAINT `post_to_author` FOREIGN KEY (`author_no`) REFERENCES `member` (`no`),
  CONSTRAINT `post_to_modifier` FOREIGN KEY (`modifier_no`) REFERENCES `member` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## API 확인 및 테스트  
1. `aplication.yml`에서 적절한 DB 포트, username, password 설정  
2. IDE에서 프로젝트 실행 후 http://localhost:8080/swagger-ui/index.html#/ 접속  

## API 설명
### members

- `[POST] members/signup` 회원가입  
비밀번호는 sha-256 알고리즘으로 암호화하여 DB에 저장합니다.  
- `[POST] members/signin` 로그인  
아이디와 비밀번호로 로그인에 성공하면 Access Token을 발행하여 반환해줍니다.  
이후 다른 작업들을 할 때 Header에 이 Access Token을 넣어주어야 합니다.  
Access Token 확인 작업은 Interceptor가 담당합니다.
- `[POST] members/signout` 로그아웃  
Header로 넘어온 Access Token을 expire합니다.
- `[DELETE] members/self` 탈퇴
- `[GET] members/self` 자신의 정보 조회
- `[PUT] members/self` 자신의 정보 수정 (이름, 비밀번호, 전화번호)
- `[GET] members/comments/self` 자신이 작성한 댓글 목록 조회  
- `[GET] members/posts/self` 자신이 작성한 게시물 조회  

### posts

- `[POST] posts` 게시물 등록  
게시물에 첨부된 파일들의 no.의 리스트를 HTTP body로 받습니다.  
해당 파일들을 게시글 no.를 이름으로 갖는 디렉토리로 옮겨줍니다. (게시글이 삭제될 때 파일들도 한 번에 삭제하기 용이하도록)  
또한 이렇게 옮겨진 파일 정보를 DB에 업데이트합니다.  
- `[GET] posts/{no}` 게시물 상세조회
- `[PUT] posts/{no}` 게시물 수정
- `[DELETE] posts/{no}` 게시물 삭제  
게시물과 연관된 파일들도 모두 삭제해줍니다.  
DB에 있는 파일 정보들도 삭제해줍니다.  
만약 등록된 파일들, 파일 정보들을 삭제하는 과정에서 예외가 발생하더라도 로그만 남기고 진행합니다. 즉, 이 경우 게시물 삭제는 롤백되지 않습니다.  
따라서, 파일이나 파일 정보 삭제가 실패한 경우 남게 되는 쓰레기 파일과 DB row는 추후 삭제해줄 필요가 있습니다.  
- `[GET] posts` 게시물 목록 조회  
- `[GET] posts/{postNo}/comments` 특정 게시물의 댓글 목록 조회

### temp

- `[POST] posts/temp` 게시물 임시저장  
새로운 게시글을 임시저장합니다.  
기존 임시저장된 글은 삭제됩니다.  
- `[GET] posts/temp` 자신의 임시저장 게시물 불러오기  
기존에 임시저장된 글의 no.를 조회합니다.  
클라이언트는 이 no.와 `[GET] posts/{no}` API를 활용해 임시저장된 게시물의 내용을 확인할 수 있습니다.  
- `[PUT] posts/temp/{no}` 임시저장 게시물 수정  
기존에 임시저장된 글의 내용을 수정합니다.  
- `[POST] posts/temp/{no}` 임시저장 게시물 최종 등록  
임시저장 게시물을 일반 게시물로 등록합니다.  

### file

- `[POST] files` 파일 등록  
새롭게 파일을 업로드하고 업로드된 파일의 no.를 반환합니다.  
클라이언트는 이 값들을 간직하고 있다가 게시물을 업로드할 때 리스트로 보내줘야 합니다.  
- `[GET] files/{no}` 파일 다운로드  
- `[DELETE] files/{no}` 파일 삭제

### comments

- `[POST] comments` 특정 게시물에 댓글 작성  
- `[PUT] comments/{commentNo}` 댓글 수정  
- `[DELETE] comments/{commentNo}` 댓글 삭제  
