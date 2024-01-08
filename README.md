# Spring_Web_Practice
This is a simple bulletin board project that supports features like user registration, authentication, post writing, comment writing, file upload/download, and temporary storage of posts.  

## Purpose
1. Learn how to use the Spring Boot framework.  
2. Practice handling exceptions for various cases.  
3. Experience trial and error while developing a bulletin board app that supports more complex features like file uploading and post draft saving, rather than just simple post submissions.  
4. Write basic unit tests for the Service Layer.  

## Used Technologies

![Spring Boot](https://img.shields.io/badge/Spring_boot-blue) ![MyBatis](https://img.shields.io/badge/MyBatis-orange) ![MySql](https://img.shields.io/badge/MySql-green)  

## DB Table Information
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

## API Verification and Testing  
1. Set the appropriate DB port, username, and password in application.yml.    
2. Run the project in your IDE and access http://localhost:8080/swagger-ui/index.html#/.    

## API Description  
### members

- `[POST] members/signup` Register as a member  
The password is encrypted with the sha-256 algorithm and stored in the database.   
- `[POST] members/signin` Login  
On successful login, an Access Token is issued and returned.    
This Access Token must be included in the Header for subsequent operations.    
An Interceptor handles Access Token verification.    
- `[POST] members/signout` Logout  
Expire the Access Token passed in the Header.  
- `[DELETE] members/self` Withdraw  
- `[GET] members/self` View your own information  
- `[PUT] members/self` Update your information (name, password, phone number)  
- `[GET] members/comments/self` View the list of comments you have written     
- `[GET] members/posts/self` View the posts you have written  

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

- `[POST] posts/temp` Save a post draft  
Saves a new post draft.  
Previous drafts are deleted.    
- `[GET] posts/temp` Retrieve your saved draft  
Retrieves the number of the previously saved draft.  
Clients can use this number and the [GET] posts/{no} API to view the draft content.  
- `[PUT] posts/temp/{no}` Edit a saved draft  
- `[POST] posts/temp/{no}` Finalize a saved draft   

### file

- `[POST] files` File upload   
Uploads a new file and returns its number.  
Clients should keep these numbers and send them as a list when uploading a post.  
- `[GET] files/{no}` Download a file  
- `[DELETE] files/{no}` Delete a file  

### comments

- `[POST] comments` Write a comment on a specific post   
- `[PUT] comments/{commentNo}` Edit a comment   
- `[DELETE] comments/{commentNo}` Delete a comment   
