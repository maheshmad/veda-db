-- MySQL dump 10.13  Distrib 5.7.8-rc, for Win64 (x86_64)
--
-- Host: localhost    Database: xe1
-- ------------------------------------------------------
-- Server version	5.7.8-rc-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chapters`
--

DROP TABLE IF EXISTS `chapters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chapters` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `courseid` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `sub_title` varchar(2000) DEFAULT NULL,
  `description` mediumtext,
  PRIMARY KEY (`id`,`courseid`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chapters_to_topics`
--

DROP TABLE IF EXISTS `chapters_to_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chapters_to_topics` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `chapter_id` int(11) NOT NULL,
  `topic_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`chapter_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `chapter_id_UNIQUE` (`chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_session_schedule`
--

DROP TABLE IF EXISTS `class_session_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class_session_schedule` (
  `session_schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `classroomid` varchar(45) NOT NULL,
  `session_start_datetime` datetime NOT NULL,
  `sesssion_end_datetime` datetime NOT NULL,
  `class_teacher_id` varchar(100) DEFAULT NULL,
  `session_current_status` varchar(45) DEFAULT 'NOT_STARTED',
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_schedule_id`),
  UNIQUE KEY `session_schedule_id_UNIQUE` (`session_schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classroom`
--

DROP TABLE IF EXISTS `classroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classroom` (
  `classroomid` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `course_record_id` varchar(100) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `sub_title` varchar(2000) DEFAULT NULL,
  `description` mediumtext,
  PRIMARY KEY (`classroomid`),
  UNIQUE KEY `id_UNIQUE` (`classroomid`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `config_id` varchar(100) NOT NULL,
  `config_group_id` varchar(100) DEFAULT NULL,
  `config_name` varchar(1000) NOT NULL,
  `config_value` varchar(20000) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_group`
--

DROP TABLE IF EXISTS `config_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_group` (
  `config_group_id` varchar(100) NOT NULL,
  `config_section_id` varchar(100) NOT NULL,
  `config_group_name` varchar(255) DEFAULT NULL,
  `footer_note` tinytext,
  `header_note` tinytext,
  `allowed_roles` varchar(2000) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`config_group_id`,`config_section_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config_section`
--

DROP TABLE IF EXISTS `config_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_section` (
  `config_section_id` varchar(100) NOT NULL,
  `config_section_name` varchar(255) DEFAULT NULL,
  `view_xtype` varchar(255) DEFAULT NULL,
  `allowed_roles` varchar(1000) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` varchar(255) DEFAULT 'CURRENT_TIMESTAMP',
  PRIMARY KEY (`config_section_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `sub_title` varchar(2000) DEFAULT NULL,
  `description` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `courses_to_chapters`
--

DROP TABLE IF EXISTS `courses_to_chapters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses_to_chapters` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `chapter_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`course_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `course_id_UNIQUE` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrollments` (
  `id` varchar(100) NOT NULL,
  `classroomid` int(11) NOT NULL,
  `user_record_id` int(11) NOT NULL,
  `enrolled_on` datetime DEFAULT CURRENT_TIMESTAMP,
  `verified_by` varchar(100) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`classroomid`,`user_record_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_attendance`
--

DROP TABLE IF EXISTS `event_attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_attendance` (
  `event_attendance_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_schedule_id` int(11) NOT NULL,
  `user_record_id` int(11) NOT NULL,
  `start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`event_attendance_id`),
  UNIQUE KEY `session_attendance_id_UNIQUE` (`event_attendance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_schedule`
--

DROP TABLE IF EXISTS `event_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_schedule` (
  `event_schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `classroomid` int(10) DEFAULT NULL,
  `start_datetime` datetime DEFAULT NULL,
  `end_datetime` datetime DEFAULT NULL,
  `event_type` varchar(100) NOT NULL,
  `event_title` varchar(1000) DEFAULT NULL,
  `event_status` varchar(100) DEFAULT 'NOT_STARTED',
  `event_description` mediumtext,
  `updated_by` varchar(100) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slides`
--

DROP TABLE IF EXISTS `slides`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slides` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `sub_title` varchar(2000) DEFAULT NULL,
  `description` mediumtext,
  `text_content` mediumtext,
  `content_image_large` longblob,
  `content_image_thumb` longblob,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  `content_image_type` varchar(10) DEFAULT NULL,
  `topicid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=631 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topics` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `chapterid` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `sub_title` varchar(2000) DEFAULT NULL,
  `description` mediumtext,
  PRIMARY KEY (`id`,`chapterid`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topics_to_slides`
--

DROP TABLE IF EXISTS `topics_to_slides`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topics_to_slides` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL,
  `slide_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`topic_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `topic_id_UNIQUE` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uploaded_files`
--

DROP TABLE IF EXISTS `uploaded_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uploaded_files` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `topicid` int(11) DEFAULT NULL,
  `content` longblob,
  `uploaded_by` varchar(255) DEFAULT NULL,
  `uploaded_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  `file_type` varchar(10) DEFAULT NULL,
  `file_processing_code` varchar(255) DEFAULT '0',
  `file_name` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_images`
--

DROP TABLE IF EXISTS `user_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_images` (
  `userid` varchar(45) NOT NULL,
  `image_id` varchar(255) NOT NULL,
  `image_type` varchar(100) DEFAULT NULL,
  `image_content_large` longblob,
  `image_content_thumb` mediumblob,
  `last_updated_by` varchar(45) DEFAULT NULL,
  `last_updated_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`,`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_sessions`
--

DROP TABLE IF EXISTS `user_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_sessions` (
  `userid` varchar(45) NOT NULL,
  `sessionid` varchar(255) NOT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  `expires_on` datetime DEFAULT NULL,
  `ip_addr` varchar(100) DEFAULT NULL,
  `client_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_record_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `emailid` varchar(255) DEFAULT NULL,
  `pswd` varchar(50) DEFAULT NULL,
  `roles` varchar(1000) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `address_line1` varchar(100) DEFAULT NULL,
  `address_line2` varchar(100) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `postalcode` varchar(10) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `cell_phone` varchar(20) DEFAULT NULL,
  `ok_to_text` tinyint(1) DEFAULT '1',
  `land_phone` varchar(20) DEFAULT NULL,
  `office_phone` varchar(20) DEFAULT NULL,
  `office_phone_ext` varchar(10) DEFAULT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime DEFAULT CURRENT_TIMESTAMP,
  `change_pswd` tinyint(1) DEFAULT '1',
  `last_login_ip` varchar(100) DEFAULT NULL,
  `account_disabled` tinyint(1) DEFAULT '0',
  `account_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_record_id`),
  UNIQUE KEY `id_UNIQUE` (`user_record_id`),
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-26  7:43:31
