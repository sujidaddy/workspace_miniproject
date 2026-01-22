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
-- Table structure for table `fish_detail`
--

DROP TABLE IF EXISTS `fish_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fish_detail` (
  `data_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_german2_ci NOT NULL,
  `detail` varchar(255) COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_german2_ci DEFAULT NULL,
  PRIMARY KEY (`data_no`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fish_detail`
--

LOCK TABLES `fish_detail` WRITE;
/*!40000 ALTER TABLE `fish_detail` DISABLE KEYS */;
INSERT INTO `fish_detail` VALUES (10,'감성돔','도미과에 속하는 해수어이다. 감성돔이라는 이름은 검은 돔에서 변화한 것으로 추정된다. 감상어, 먹도미, 감성도미, 감셍이, 구릿, 맹이, 남정바리 등으로 불리기도 한다.','https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Black_seabream%28side.JPG/250px-Black_seabream%28side.JPG'),(12,'농어',' 주걱치목 농어과에 속하는 물고기이다. 농어목으로 분류하기도 한다. 최근 심해 탐험중 밝혀진 결과로는 몸길이 최대 2m가 넘는다고 한다. 몸은 약간 길고 납작하며 등은 푸른빛이 도는 회색, 배는 은백색이다.','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Suzuki201302.jpg/250px-Suzuki201302.jpg'),(13,'돌돔','검정우럭목 돌돔과에 속하는 대형 육식 물고기이다.[1] 식용이나 낚시 대상으로 인기가 많다. 농어목으로 분류하기도 한다. 한국, 일본 연해에 많이 분포한다.','https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/K231003%EB%8F%8C%EB%8F%94.jpg/250px-K231003%EB%8F%8C%EB%8F%94.jpg'),(14,'우럭','쏨뱅이목 양볼락과에 속하는, 아시아 북부에서 볼 수 있는 바닷물고기이다. 어린 것의 몸색은 검은색이며 나이가 듦에 따라 측면에 얼룰덜룩한 회색으로 바뀌면서 흰색에 가까워진다.','https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Sebastes_schlegelii_Hilgendorf%2C_1880.jpg/250px-Sebastes_schlegelii_Hilgendorf%2C_1880.jpg'),(15,'참돔','도미과의 물고기이다.','https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Pagrus_major_Red_seabream_ja01.jpg/250px-Pagrus_major_Red_seabream_ja01.jpg'),(16,'벵에돔','벵에돔과에 속하는 물고기로 동아시아의 온대 바다에 서식한다.[1]몸길이는 60cm이다.','https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/%E3%83%A1%E3%82%B8%E3%83%8A_%2815257760119%29.jpg/250px-%E3%83%A1%E3%82%B8%E3%83%8A_%2815257760119%29.jpg'),(17,'볼락','페르카목 쏨뱅이과에 속하는 조기어류의 일종이다. 몸길이 20-30cm이다.','https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Sebastes_Inermis.jpg/250px-Sebastes_Inermis.jpg'),(18,'열기','불볼락은 일명 열기로 불리기도 하는데, 그물과 주낙으로 주로 잡는 어종이다. 매운탕과 회로도 먹지만, 내장과 뼈를 제거하고, 해수에 염장한 뒤 해풍에 말려 구워먹기도 한다.','https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Sebastes_thompsoni.JPG/250px-Sebastes_thompsoni.JPG'),(176,'기타어종','바닷물고기 또는 해수어(海水魚)는 바다에서 사는 어류를 말한다.','https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Georgia_Aquarium_-_Giant_Grouper_edit.jpg/250px-Georgia_Aquarium_-_Giant_Grouper_edit.jpg'),(177,'선상낚시',NULL,NULL),(178,'바다낚시',NULL,NULL);
/*!40000 ALTER TABLE `fish_detail` ENABLE KEYS */;
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
