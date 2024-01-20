-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: userservice
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authentications`
--

DROP TABLE IF EXISTS `authentications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authentications`
(
    `id`            int                                                           NOT NULL AUTO_INCREMENT,
    `username`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL,
    `refresh_token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`),
    CONSTRAINT `authentications_users_username_fk` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentications`
--

LOCK
TABLES `authentications` WRITE;
/*!40000 ALTER TABLE `authentications` DISABLE KEYS */;
INSERT INTO `authentications`
VALUES (5, 'user0001',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMSIsImlhdCI6MTcwNDY1MTU2OSwiZXhwIjoxNzA3MjQzNTY5fQ.h4GkHX6ExkrF2EgU9-m03GY2g8ySLiKYS4lvwtvsp2c'),
       (7, 'user0002',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMiIsImlhdCI6MTcwNTcxMzAxNiwiZXhwIjoxNzA4MzA1MDE2fQ.WuwGmCklU_5tewBrrW6oiZrdwQeDcs1u8Uu6PRUN0xY'),
       (8, 'user0003',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMyIsImlhdCI6MTcwNTcxMzcyOSwiZXhwIjoxNzA4MzA1NzI5fQ.i6Q55IcA4IICUYxKAe05ACI-tmsIUVH3U8nI-jc4U0A'),
       (9, 'user0004',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwNCIsImlhdCI6MTcwNTcxMzc5NCwiZXhwIjoxNzA4MzA1Nzk0fQ.lOx4whS9KvsrurBV2PrNFTwcKdtnuNUkxrsDwqZpKyM');
/*!40000 ALTER TABLE `authentications` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `bookmark_details`
--

DROP TABLE IF EXISTS `bookmark_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmark_details`
(
    `bookmark_id` int    NOT NULL,
    `target_id`   int    NOT NULL,
    `type`        bit(1) NOT NULL,
    PRIMARY KEY (`bookmark_id`, `target_id`, `type`),
    CONSTRAINT `bookmark_details_bookmarks_id_fk` FOREIGN KEY (`bookmark_id`) REFERENCES `bookmarks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmark_details`
--

LOCK
TABLES `bookmark_details` WRITE;
/*!40000 ALTER TABLE `bookmark_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmark_details` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks`
(
    `id`       int                                                          NOT NULL AUTO_INCREMENT,
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `bookmarks_pk` (`username`),
    CONSTRAINT `bookmarks_users_username_fk99` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK
TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows`
(
    `follower` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `followed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`follower`, `followed`),
    KEY        `follows_users_username_fk` (`followed`),
    CONSTRAINT `follows_users_username_fk` FOREIGN KEY (`followed`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `follows_users_username_fk2` FOREIGN KEY (`follower`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK
TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows`
VALUES ('user0002', 'user0001');
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users`
(
    `id`           int                                                           NOT NULL AUTO_INCREMENT,
    `username`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL,
    `password`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `email`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL,
    `birthdate`    date                                                          DEFAULT NULL,
    `display_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    `bio`          tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `gender`       bit(1)                                                        DEFAULT NULL,
    `role`         enum('ROLE_member','ROLE_admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`),
    UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK
TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users`
VALUES (10, 'user0001', '$2a$10$EsGrvTTapX4FTLISQ7q9Fe3HMOyN29Lh4SqBSanvhdbHQEKBO5Q2G', 'user0001@gmail.com', NULL,
        'Phạm Văn Vinh', NULL, NULL, 'ROLE_member'),
       (11, 'user0002', '$2a$10$FdXsqRycBCTDHCt8MMchSeApd6zERMQS6R2amrsNVsxcaJifADclO', 'user0002@gmail.com', NULL,
        'Nguyễn Thành Quốc', NULL, NULL, 'ROLE_member'),
       (12, 'user0003', '$2a$10$dqOummWyouRUUWNvUDqCP.XpbMKXNF4EHDsHTgr8MBgz/1126mynq', 'user0003@gmail.com', NULL,
        'Nguyễn Văn Lương', NULL, NULL, 'ROLE_member'),
       (13, 'user0004', '$2a$10$Gnoy/ke8zTy9GnU0UfeABeuFAjxuDTWnrGcIx535IIdnoO1Oz7TyW', 'user0004@gmail.com', NULL,
        'Nguyễn Văn Tấn', NULL, NULL, 'ROLE_member');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `votes`
(
    `target_id`   int                                                          NOT NULL,
    `target_type` bit(1)                                                       NOT NULL,
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `vote_type`   bit(1)                                                       NOT NULL,
    `updated_at`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`target_id`, `target_type`, `username`),
    KEY           `FK_VOTES_USERNAME` (`username`),
    CONSTRAINT `FK_VOTES_USERNAME` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votes`
--

LOCK
TABLES `votes` WRITE;
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
INSERT INTO `votes`
VALUES (130, _binary '', 'user0003', _binary '\0', '2024-01-20 09:40:07');
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;
UNLOCK
TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-20 20:46:50
