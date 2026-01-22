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
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `user_no` bigint NOT NULL AUTO_INCREMENT,
  `userid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_MEMBER') NOT NULL,
  `enabled` bit(1) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `last_login_time` datetime(6) NOT NULL,
  `google` varchar(255) DEFAULT NULL,
  `naver` varchar(255) DEFAULT NULL,
  `kakao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_no`),
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (41,'administrator','$2a$10$xkm.lG5iyJh1ejX20fPqx.xYULwTSoLs0gkkFnOou4Ji9JtJdaLeK','관리자','admin@miniproject.kdigital.com','ROLE_ADMIN',_binary '','2025-11-30 15:00:00.000000','2026-01-21 08:40:43.353371',NULL,NULL,NULL),(44,'kyuhuhu','$2a$10$NgN/yGVpfGYBDHUW1TqyzeG6.XO4R0EJdiTycvaAirV7l3D.8NF/.','박민규','kyuhuhu@naver.com','ROLE_MEMBER',_binary '','2026-01-05 03:36:18.588591','2026-01-05 03:36:18.588591','google*박민규*kyuhuhu.sujidaddy@gmail.com',NULL,NULL),(45,'testuser1','$2a$10$pMsC3ROZ26u5dEYZidoFJO9d6O3RcGppQg97trHv3bEtZxC66cJru','테스트유저1','testuser1@naver.com','ROLE_MEMBER',_binary '','2026-01-07 05:13:18.780908','2026-01-07 05:13:18.780908',NULL,NULL,NULL),(46,'testuser2','$2a$10$tkfzr1g6B7Iq.EPAmNfVUuFCCZ6haSjvBUe9aO/luJtWZ6gf4J8Oe','테스트유저2','testuser2@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:19:58.971306','2026-01-07 05:19:58.971306',NULL,NULL,NULL),(47,'testuser3','$2a$10$Fy7EpIMybBOzwObb5AIp9.Ouw5H0pMqIQRuyhmLHRSJ.z2p14koDC','테스트유저3','testuser3@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:11.706279','2026-01-07 05:20:11.706279',NULL,NULL,NULL),(48,'testuser4','$2a$10$gErOPPX2FLgWpNq37RrSbON0ZrF88H7/I5j6yPY3w.CIWjG5dr1pu','테스트유저4','testuser4@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:18.966983','2026-01-07 05:20:18.966983',NULL,NULL,NULL),(49,'testuser5','$2a$10$8eN7E9tC4LOuFHj85Q2lKe.xc78oTXd5P7s1H9iVsO7ug8/wDAQVy','테스트유저5','testuser5@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:22.454669','2026-01-07 05:20:22.454669',NULL,NULL,NULL),(50,'testuser6','$2a$10$ivDFoXEYaptd1BGqZZmwceSd0./2r9GxdWCIm8wqJcUdqYVfU0uXy','테스트유저6','testuser6@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:26.428419','2026-01-07 05:20:26.428419',NULL,NULL,NULL),(51,'testuser7','$2a$10$IsBuIe9R6voynWWEODAWye9fTPbYhqQWilzBpUTnObp3Xrydqd14a','테스트유저7','testuser7@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:29.938569','2026-01-07 05:20:29.938569',NULL,NULL,NULL),(52,'testuser8','$2a$10$JE9DxGXlbWuyu5S37BzJg.LMGvaAKbs3VMTKLwGUP.D4h3RsLmjl2','테스트유저8','testuser8@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:34.164731','2026-01-07 05:20:34.164731',NULL,NULL,NULL),(53,'testuser9','$2a$10$UDsI5A8WFQIXpzweCsYlQ.EyFryunBV.BLGeH.QDrqPPjiXlQ/32i','테스트유저9','testuser9@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:37.404658','2026-01-07 05:20:37.404658',NULL,NULL,NULL),(54,'testuser10','$2a$10$vPKJDAM6NGMXX4lropDQq.QJkq0pWyuAI02vd7oFMcph0D2hX7JDy','테스트유저10','testuser10@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:41.785641','2026-01-07 05:20:41.785641',NULL,NULL,NULL),(55,'testuser11','$2a$10$zb1LVrYgg.Z1wAvJFTXEzO/MwQvieIo4Tq2AR1mHcHx2NQKXuCn.6','테스트유저11','testuser11@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:44.575459','2026-01-07 05:20:44.575459',NULL,NULL,NULL),(56,'testuser12','$2a$10$E1T69S80.kg43fF17013W.3N7hxH0KvVtOqWHvQ3mTJ.viiCH1gke','테스트유저12','testuser12@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:47.426936','2026-01-07 05:20:47.426936',NULL,NULL,NULL),(57,'testuser13','$2a$10$exjLGss5OOoHakeq7uVb1.ZJU65t5XLHU8qsSEkaAOR4kK5nzXfua','테스트유저13','testuser13@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:50.101850','2026-01-07 05:20:50.101850',NULL,NULL,NULL),(58,'testuser14','$2a$10$b95bP3jb33GMS2TMC9AWgu78/3M3t7B7baRUYLt539uq87R03ekn.','테스트유저14','testuser14@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:54.596973','2026-01-07 05:20:54.596973',NULL,NULL,NULL),(59,'testuser15','$2a$10$kiLFyy1ETRHrjcMkBddWqegJ78doixr8rAt.qxUZuNP9Rby8HLy6O','테스트유저15','testuser15@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:20:57.933707','2026-01-07 05:20:57.933707',NULL,NULL,NULL),(60,'testuser16','$2a$10$GC4tKPytngAZGLiVMUrhx.iDf3QRVptrN5SSPCAaaT9r74OoNrGUS','테스트유저16','testuser16@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:21:01.433179','2026-01-07 05:21:01.433179',NULL,NULL,NULL),(61,'testuser17','$2a$10$tlnPlv8IwUelN81SU2xFw.FUXjrqiq5aFJWalv5v2yaDNf7N7o.1S','테스트유저17','testuser17@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:21:04.963958','2026-01-07 05:21:04.963958',NULL,NULL,NULL),(62,'testuser18','$2a$10$UH9YGlZ7T86tJvaMI4w5su8BONYamvP./qHAHl9W8et5xMKoFG2ZK','테스트유저18','testuser18@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:21:08.840428','2026-01-07 05:21:08.840428',NULL,NULL,NULL),(63,'testuser19','$2a$10$eNZVPHYGJmicdo08J66R9OhNVTJWxKLOa8dMi3bHu6uITISKvCVOW','테스트유저19','testuser19@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:21:12.752252','2026-01-07 05:21:12.752252',NULL,NULL,NULL),(64,'testuser20','$2a$10$xoEgGCDKQ8St04eeTz7eluFHQBPhxU2NzAKYik8TYdNM7GaT5SDcO','테스트유저20','testuser20@example.com','ROLE_MEMBER',_binary '','2026-01-07 05:21:16.744138','2026-01-07 05:21:16.744138',NULL,NULL,NULL),(65,'admin','$2a$10$1nkyQRsaOyv3vIgpPYxY5uuQ42yZch5d3JeFeZAyif30VF28Cue4e','admin','admin@miniproject.kdigital.com','ROLE_ADMIN',_binary '','2025-11-30 15:00:00.000000','2026-01-22 04:40:00.718917',NULL,NULL,NULL);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-22 13:54:10
