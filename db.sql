.# DB 세팅
DROP DATABASE IF EXISTS `readit`;
CREATE DATABASE `readit`;
USE `readit`;

# member 테이블 세팅
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME,
    updateDate DATETIME,
    delDate DATETIME,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	authStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    loginId CHAR(20) NOT NULL UNIQUE,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(20) NOT NULL,
    `nickname` CHAR(20) NOT NULL,
    `email` CHAR(100) NOT NULL
);


DROP TABLE IF EXISTS attr;
CREATE TABLE attr (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    `name` CHAR(100) NOT NULL UNIQUE,
    `value` TEXT NOT NULL
);

# attr 테이블에서 name 을 4가지 칼럼으로 나누기
ALTER TABLE `attr` DROP COLUMN `name`,
ADD COLUMN `relTypeCode` CHAR(20) NOT NULL AFTER `updateDate`,
ADD COLUMN `relId` INT(10) UNSIGNED NOT NULL AFTER `relTypeCode`,
ADD COLUMN `typeCode` CHAR(30) NOT NULL AFTER `relId`,
ADD COLUMN `type2Code` CHAR(30) NOT NULL AFTER `typeCode`,
CHANGE `value` `value` TEXT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL AFTER `type2Code`,
DROP INDEX `name`; 

# attr 유니크 인덱스 걸기
## 중복변수 생성금지
## 변수찾는 속도 최적화
ALTER TABLE `attr` ADD UNIQUE INDEX (`relTypeCode`, `relId`, `typeCode`, `type2Code`); 

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE `attr` ADD INDEX (`relTypeCode`, `typeCode`, `type2Code`); 

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  title CHAR(200) NOT NULL,
  `body` LONGTEXT NOT NULL,
  memberId INT(10) UNSIGNED NOT NULL,
  `cateId` INT(10) NOT NULL,
  `boardId` INT(10) NOT NULL DEFAULT 1
);

CREATE TABLE category (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` CHAR(100) NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  boardId INT(10) UNSIGNED NOT NULL
);

CREATE TABLE board (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `code` CHAR(100) NOT NULL UNIQUE,
  `name` CHAR(100) NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

INSERT INTO board
SET `code` = 'free',
`name` = '자유게시판',
regDate = NOW(),
updateDate = NOW();

INSERT INTO board
SET `code` = 'notice',
`name` = '공지사항',
regDate = NOW(),
updateDate = NOW();

SELECT * FROM article

# 코드는 unique
SELECT B.code
FROM board AS B
INNER JOIN MEMBER AS M
ON B.memberId = M.id

# name은 변경 가능
SELECT B.name
FROM board AS B
INNER JOIN MEMBER AS M
ON B.memberId = M.id


SELECT A.*
FROM article AS A
INNER JOIN `Member` AS M
ON M.id = A.memberId
AND A.memberId = 1


SELECT * FROM board

SELECT * FROM article

#===========================================================

CREATE TABLE `file` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME,
    updateDate DATETIME,
    delDate DATETIME,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	relTypeCode CHAR(50) NOT NULL,
	relId INT(10) UNSIGNED NOT NULL,
    originFileName VARCHAR(100) NOT NULL,
    fileExt CHAR(10) NOT NULL,
    typeCode CHAR(20) NOT NULL,
    type2Code CHAR(20) NOT NULL,
    fileSize INT(10) UNSIGNED NOT NULL,
    fileExtTypeCode CHAR(10) NOT NULL,
    fileExtType2Code CHAR(10) NOT NULL,
    fileNo TINYINT(2) UNSIGNED NOT NULL,
    `body` LONGBLOB
); 

#===========================================================
#0830

ALTER TABLE `member`
ADD COLUMN `tempPwStatus` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;

#===========================================================
#0831

INSERT INTO category
SET `name` = '기타',
regDate = NOW(),
updateDate = NOW(),
boardId = 1;

INSERT INTO category
SET `name` = '공지',
regDate = NOW(),
updateDate = NOW(),
boardId = 2;

#===========================================================
#0901

DROP TABLE IF EXISTS attr;
CREATE TABLE attr (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    `relTypeCode` CHAR(20) NOT NULL,
    `relId` INT(10) UNSIGNED NOT NULL,
    `typeCode` CHAR(30) NOT NULL,
    `type2Code` CHAR(30) NOT NULL,
    `value` TEXT NOT NULL
);

# attr 유니크 인덱스 걸기
## 중복변수 생성금지
## 변수찾는 속도 최적화
ALTER TABLE `attr` ADD UNIQUE INDEX (`relTypeCode`, `relId`, `typeCode`, `type2Code`); 

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE `attr` ADD INDEX (`relTypeCode`, `typeCode`, `type2Code`);

# attr에 만료날짜 추가
ALTER TABLE `attr` ADD COLUMN `expireDate` DATETIME NULL AFTER `value`;

# 해당 부분 memberController - findLoginPw 에서 attr 이용하는 것으로 수정.
ALTER TABLE `member` DROP COLUMN `tempPwStatus`;

CREATE TABLE reply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME,
    updateDate DATETIME,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME,
    displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    `body` LONGTEXT NOT NULL
);

#===========================================================
#0903

DROP TABLE IF EXISTS series;
CREATE TABLE series (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME,
    updateDate DATETIME,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME,
    displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
    memberId INT(10) UNSIGNED NOT NULL,
    cateId INT(10) UNSIGNED NOT NULL,
    `name` CHAR(100) NOT NULL
);

ALTER TABLE `article`
ADD COLUMN `seriesId` INT(10) UNSIGNED;

INSERT INTO series
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
cateId = 1,
`name` = '첫번째 시리즈';

#===========================================================
#0904
ALTER TABLE `article` DROP COLUMN `seriesId`,
ADD COLUMN `seriesId` INT(10) UNSIGNED NOT NULL DEFAULT 0;


INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '로맨스';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '판타지';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '로맨스판타지';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '무협';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '역사';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '게임';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '스포츠';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '패러디';

INSERT INTO category
SET regDate = NOW(),
updateDate = NOW(),
`name` = '일반문학';



#===========================================================
#0905
#member의 nickname을 unique로 변경

CREATE TABLE novel (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` CHAR(100) NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  cateId INT(10) UNSIGNED NOT NULL,
  memberId INT(10) UNSIGNED NOT NULL
);

DROP TABLE IF EXISTS chapter;
CREATE TABLE chapter (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title CHAR(100) NOT NULL UNIQUE,
  `body` LONGTEXT NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  cateId INT(10) UNSIGNED NOT NULL,
  memberId INT(10) UNSIGNED NOT NULL,
  novelId INT(10) UNSIGNED NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS category;
CREATE TABLE category (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` CHAR(100) NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  displayStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 1
);

#===========================================================
#0908
ALTER TABLE `novel`
ADD COLUMN `seriesStatus` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;

#===========================================================
#0911
ALTER TABLE chapter DROP COLUMN cateId;

#===========================================================
#0912
ALTER TABLE `reply` DROP COLUMN `articleId`,
ADD COLUMN `relType` CHAR(20) NOT NULL AFTER `memberID`,
ADD COLUMN `relId` INT(10) UNSIGNED NOT NULL AFTER `relType`;

#===========================================================
#0916
ALTER TABLE `article`
ADD COLUMN hit INT(10) UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE `chapter`
ADD COLUMN hit INT(10) UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE `novel`
ADD COLUMN totalHit INT(10) UNSIGNED NOT NULL DEFAULT 0;

#===========================================================
#0917
ALTER TABLE `novel` 
ADD COLUMN totalCh INT(10) UNSIGNED NOT NULL DEFAULT 0; 

#===========================================================
#0918
ALTER TABLE `reply` 
ADD COLUMN secretStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0; 