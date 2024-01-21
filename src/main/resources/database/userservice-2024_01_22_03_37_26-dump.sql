-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: userservice
-- ------------------------------------------------------
-- Server version	8.2.0

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
CREATE TABLE `authentications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `refresh_token` varchar(200) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_i2g7gfbs6hf03r304d6lv8hvj` (`username`),
  CONSTRAINT `FKp78h4mwju0x0yd7pgndruc13t` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentications`
--

LOCK TABLES `authentications` WRITE;
/*!40000 ALTER TABLE `authentications` DISABLE KEYS */;
INSERT INTO `authentications` VALUES (3,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdW9uZ25ndXllbiIsImlhdCI6MTcwNTg1OTE4NSwiZXhwIjoxNzA4NDUxMTg1fQ.CDnso5ohBm_pz6BGsEsZfWsNCILWOiqUp2s1srPauqg','luongnguyen'),(4,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFuaHF1b2MiLCJpYXQiOjE3MDU4NjMwMjgsImV4cCI6MTcwODQ1NTAyOH0.uzvvhCIbsyKiVckmvBQRFl0R9mwnPA2UwEhiC3xZ7BM','thanhquoc'),(5,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YW52aW5oNyIsImlhdCI6MTcwNTg2NTYwMiwiZXhwIjoxNzA4NDU3NjAyfQ.DwYzdtaGywX_YRdCYt9Dw8zz-7T2i6j8btFIvsYykPE','vanvinh7');
/*!40000 ALTER TABLE `authentications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `follower` varchar(50) NOT NULL,
  `followed` varchar(50) NOT NULL,
  PRIMARY KEY (`followed`,`follower`),
  KEY `FKjnqt4f5bti6niw7afunse4de7` (`follower`),
  CONSTRAINT `FKjkcxs3b09wycopcoihen12a5g` FOREIGN KEY (`followed`) REFERENCES `users` (`username`),
  CONSTRAINT `FKjnqt4f5bti6niw7afunse4de7` FOREIGN KEY (`follower`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES ('vanvinh7','luongnguyen'),('vanvinh7','thanhquoc');
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(100) DEFAULT NULL,
  `bio` longtext,
  `birthdate` date DEFAULT NULL,
  `display_name` varchar(100) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `gender` bit(1) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `role` enum('ROLE_member','ROLE_admin') NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (3,NULL,NULL,NULL,'Nguyễn Văn lương','user0002@gmail.com',NULL,'$2a$10$mUAukqbUM1Su5UJc5ncL/.RoHN3XHx.G2tAAzSsnGgENCD4NF71m6','ROLE_member','luongnguyen'),(4,NULL,NULL,NULL,'Nguyễn Thành Quốc','user0001@gmail.com',NULL,'$2a$10$DOJEflsPF19Prhzolz9bMeV/70V982SXeeWazP7344JCGClVe99PS','ROLE_member','thanhquoc'),(5,'http://localhost:8892/api/images/1.jpg','# Xin chào! 👋\n\nTôi là một sinh viên ngành Công nghệ thông tin tại **Trường Đại học Quy Nhơn (QNU)**. Tôi đang theo đuổi niềm đam mê về lập trình và công nghệ.\n\n## Học vấn 🎓\n\nTôi đang theo học chương trình Công nghệ thông tin tại QNU, nơi tôi được trang bị kiến thức chuyên sâu về lập trình, hệ thống, và phân tích dữ liệu.\n\n## Kỹ năng 💻\n\nTôi có kinh nghiệm làm việc với nhiều ngôn ngữ lập trình như Java, C#, Javascript. Tôi cũng đã tham gia nhiều dự án nhóm, nơi tôi phát triển kỹ năng làm việc nhóm và giải quyết vấn đề.\n\n## Mục tiêu tương lai 🚀\n\nTôi mong muốn trở thành một lập trình viên giỏi, tạo ra những sản phẩm công nghệ có thể giúp cải thiện cuộc sống của mọi người.\n\nRất vui được làm quen với mọi người!','2002-12-10','Phạm Văn Vinh','phamvanvinh@gmail.com',_binary '','$2a$10$CJ50Os0Za1q/HOt0tcmNGOLpLn2dA54ciMxbG44hO/7h4s4zALpk.','ROLE_member','vanvinh7');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22  3:37:27
