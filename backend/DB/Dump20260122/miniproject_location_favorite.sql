-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: miniproject
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `location_favorite`
--

DROP TABLE IF EXISTS `location_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_favorite` (
  `favorite_no` bigint NOT NULL AUTO_INCREMENT,
  `user_no` bigint NOT NULL,
  `location_no` bigint NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `delete_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`favorite_no`),
  KEY `FKahe46xwgia5w9bw20toyleifb` (`location_no`),
  KEY `FKrolrrn2bjwodnieewaj2xqm3f` (`user_no`),
  CONSTRAINT `FKahe46xwgia5w9bw20toyleifb` FOREIGN KEY (`location_no`) REFERENCES `location` (`location_no`),
  CONSTRAINT `FKrolrrn2bjwodnieewaj2xqm3f` FOREIGN KEY (`user_no`) REFERENCES `member` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_favorite`
--

LOCK TABLES `location_favorite` WRITE;
/*!40000 ALTER TABLE `location_favorite` DISABLE KEYS */;
INSERT INTO `location_favorite` VALUES (1,44,3,'2026-01-05 03:36:59.645154',NULL),(2,44,11,'2026-01-05 03:37:00.346935',NULL),(3,44,12,'2026-01-05 03:37:01.003268',NULL),(4,44,13,'2026-01-05 03:37:01.653841',NULL),(5,41,7,'2026-01-09 08:16:05.181721',NULL);
/*!40000 ALTER TABLE `location_favorite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-22 13:54:09
