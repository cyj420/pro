SELECT * FROM `member`;
SELECT * FROM article;

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
    `email` CHAR(100) NOT NULL UNIQUE
);

# 댓글 테이블 추가
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
  cateId INT(10) UNSIGNED NOT NULL
);

CREATE TABLE board (
  `id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` CHAR(100) NOT NULL,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);