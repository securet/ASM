-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: asm_new_test
-- ------------------------------------------------------
-- Server version	5.7.20-0ubuntu0.16.04.1

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
-- Table structure for table `app_notification`
--

DROP TABLE IF EXISTS `app_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_notification` (
  `notificationId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `fromDate` datetime DEFAULT NULL,
  `isAppUpdate` bit(1) NOT NULL,
  `maxAppVersion` double NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `minAppVersion` double NOT NULL,
  `thruDate` datetime DEFAULT NULL,
  PRIMARY KEY (`notificationId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset` (
  `assetId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `assetTag` varchar(255) NOT NULL,
  `installedDate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `assetTypeId` int(11) DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  PRIMARY KEY (`assetId`),
  UNIQUE KEY `UK_t8kob8mr8fxn2ghi1l2cgxli9` (`assetTag`),
  KEY `FK_c1kshr6folqh045a89pk8s1ml` (`assetTypeId`),
  KEY `FK_3pg711pnlt4t6efl8h7uuv505` (`siteId`),
  CONSTRAINT `FK_3pg711pnlt4t6efl8h7uuv505` FOREIGN KEY (`siteId`) REFERENCES `site` (`siteId`),
  CONSTRAINT `FK_c1kshr6folqh045a89pk8s1ml` FOREIGN KEY (`assetTypeId`) REFERENCES `asset_type` (`assetTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_type`
--

DROP TABLE IF EXISTS `asset_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_type` (
  `assetTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`assetTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_user_site`
--

DROP TABLE IF EXISTS `client_user_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_user_site` (
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `siteId` int(11) NOT NULL,
  `userId` varchar(255) NOT NULL,
  PRIMARY KEY (`siteId`,`userId`),
  KEY `FK_en28863xmllp06w0faa0lhque` (`userId`),
  CONSTRAINT `FK_dg50ref71jbtho58f6dgc7bpd` FOREIGN KEY (`siteId`) REFERENCES `site` (`siteId`),
  CONSTRAINT `FK_en28863xmllp06w0faa0lhque` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enumeration`
--

DROP TABLE IF EXISTS `enumeration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enumeration` (
  `enumerationId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `enumDescription` varchar(255) DEFAULT NULL,
  `enumTypeId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`enumerationId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `geo`
--

DROP TABLE IF EXISTS `geo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `geo` (
  `geoId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `geoType` int(11) DEFAULT NULL,
  `geoName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`geoId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `geo_assoc`
--

DROP TABLE IF EXISTS `geo_assoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `geo_assoc` (
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `assocType` varchar(255) DEFAULT NULL,
  `geoIdTo` varchar(255) NOT NULL,
  `geoId` varchar(255) NOT NULL,
  PRIMARY KEY (`geoIdTo`,`geoId`),
  KEY `FK_k7q71j844phgwq4du4nr602h4` (`geoId`),
  CONSTRAINT `FK_jbxude1ojencqn7jkteyrksdv` FOREIGN KEY (`geoIdTo`) REFERENCES `geo` (`geoId`),
  CONSTRAINT `FK_k7q71j844phgwq4du4nr602h4` FOREIGN KEY (`geoId`) REFERENCES `geo` (`geoId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `issue_type`
--

DROP TABLE IF EXISTS `issue_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_type` (
  `issueTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `issueGroup` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `serviceTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`issueTypeId`),
  KEY `FK_l2clwhmr3wvv786viy4sp0315` (`serviceTypeId`),
  CONSTRAINT `FK_l2clwhmr3wvv786viy4sp0315` FOREIGN KEY (`serviceTypeId`) REFERENCES `service_type` (`serviceTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mail_template`
--

DROP TABLE IF EXISTS `mail_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_template` (
  `templateName` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `bcc` varchar(255) DEFAULT NULL,
  `cc` varchar(255) DEFAULT NULL,
  `contentType` varchar(255) DEFAULT NULL,
  `mailFrom` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `templateFileName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`templateName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `moduleId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`moduleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `organizationId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `organizationType` varchar(255) DEFAULT NULL,
  `shortDesc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`organizationId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `part_order_request`
--

DROP TABLE IF EXISTS `part_order_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part_order_request` (
  `partOrderRequestId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `cost` decimal(19,2) NOT NULL,
  `initiatedBy` varchar(255) DEFAULT NULL,
  `respondedBy` varchar(255) DEFAULT NULL,
  `sparePartId` int(11) NOT NULL,
  `statusId` varchar(255) NOT NULL,
  `ticketId` varchar(255) NOT NULL,
  PRIMARY KEY (`partOrderRequestId`),
  KEY `FK_ra9j1bhoncrmj0t64q42k1npu` (`initiatedBy`),
  KEY `FK_cj8tttmtgnupu90wd0al7maag` (`respondedBy`),
  KEY `FK_k07o4v8bgort6vcepqeoof083` (`sparePartId`),
  KEY `FK_jw2sdr072qsjb4eojgv5t1kyj` (`statusId`),
  KEY `FK_b6ajf2pntooxximejuu8678rv` (`ticketId`),
  CONSTRAINT `FK_b6ajf2pntooxximejuu8678rv` FOREIGN KEY (`ticketId`) REFERENCES `ticket` (`ticketId`),
  CONSTRAINT `FK_cj8tttmtgnupu90wd0al7maag` FOREIGN KEY (`respondedBy`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_jw2sdr072qsjb4eojgv5t1kyj` FOREIGN KEY (`statusId`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_k07o4v8bgort6vcepqeoof083` FOREIGN KEY (`sparePartId`) REFERENCES `service_spare_part` (`sparePartId`),
  CONSTRAINT `FK_ra9j1bhoncrmj0t64q42k1npu` FOREIGN KEY (`initiatedBy`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `permissionId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_type`
--

DROP TABLE IF EXISTS `role_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_type` (
  `roleTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `roleName` varchar(255) NOT NULL,
  `roleType` varchar(255) NOT NULL,
  PRIMARY KEY (`roleTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence_generator`
--

DROP TABLE IF EXISTS `sequence_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_generator` (
  `sequenceName` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `sequenceValue` bigint(20) NOT NULL,
  PRIMARY KEY (`sequenceName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_spare_part`
--

DROP TABLE IF EXISTS `service_spare_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_spare_part` (
  `sparePartId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `cost` decimal(19,2) NOT NULL,
  `partDescription` varchar(255) NOT NULL,
  `partName` varchar(255) NOT NULL,
  `vendorOrganizationId` int(11) NOT NULL,
  PRIMARY KEY (`sparePartId`),
  UNIQUE KEY `UK_mauggad5d17ot9qeg263nnmtn` (`vendorOrganizationId`,`partName`),
  CONSTRAINT `FK_225mprlfbqtyet6xrs03sns64` FOREIGN KEY (`vendorOrganizationId`) REFERENCES `organization` (`organizationId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_type`
--

DROP TABLE IF EXISTS `service_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_type` (
  `serviceTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`serviceTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site` (
  `siteId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) NOT NULL,
  `siteType` varchar(255) DEFAULT NULL,
  `circle` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `moduleId` int(11) DEFAULT NULL,
  `organizationId` int(11) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`siteId`),
  KEY `FK_2pevctm22614jtu5vkksohbjs` (`circle`),
  KEY `FK_sr7ontg2r7ks0vhambdsgp98f` (`city`),
  KEY `FK_smekxo2i7gwo85b8cka0g0sc0` (`moduleId`),
  KEY `FK_avxsynij3r8lufjlsefjxjfhm` (`organizationId`),
  KEY `FK_r5lub395w6gp42hjtg2xcu85o` (`state`),
  CONSTRAINT `FK_2pevctm22614jtu5vkksohbjs` FOREIGN KEY (`circle`) REFERENCES `geo` (`geoId`),
  CONSTRAINT `FK_avxsynij3r8lufjlsefjxjfhm` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`),
  CONSTRAINT `FK_r5lub395w6gp42hjtg2xcu85o` FOREIGN KEY (`state`) REFERENCES `geo` (`geoId`),
  CONSTRAINT `FK_smekxo2i7gwo85b8cka0g0sc0` FOREIGN KEY (`moduleId`) REFERENCES `module` (`moduleId`),
  CONSTRAINT `FK_sr7ontg2r7ks0vhambdsgp98f` FOREIGN KEY (`city`) REFERENCES `geo` (`geoId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `ticketId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `description` text NOT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `shortDesc` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `ticketMasterId` varchar(255) DEFAULT NULL,
  `assetId` int(11) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  `issueTypeId` int(11) DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `reporterUserId` varchar(255) DEFAULT NULL,
  `resolverUserId` varchar(255) DEFAULT NULL,
  `serviceTypeId` int(11) NOT NULL,
  `severity` varchar(255) DEFAULT NULL,
  `siteId` int(11) NOT NULL,
  `statusId` varchar(255) DEFAULT NULL,
  `ticketType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ticketId`),
  UNIQUE KEY `UK_g4oesw3camdi82kokmdd9fkg6` (`ticketMasterId`),
  KEY `FK_7w6cck3isi9w76nxvsjrinmq0` (`assetId`),
  KEY `FK_s3m689xufr99ta919k0mirgvq` (`createdBy`),
  KEY `FK_oess3lv382smng08jufn7em6m` (`issueTypeId`),
  KEY `FK_nu24hqr1jox6dn9k094is921u` (`modifiedBy`),
  KEY `FK_3pr4eim8lxc1shpm7x3lmf8dk` (`priority`),
  KEY `FK_lnmo91jy7a1ilx8kiybj19pfa` (`reporterUserId`),
  KEY `FK_peam4jyjf9fcveqh1u3aw37vg` (`resolverUserId`),
  KEY `FK_kq2qinnrd2os4pmvb7aiwo357` (`serviceTypeId`),
  KEY `FK_jfvr399866v36w84303rm7s3u` (`severity`),
  KEY `FK_h52mjcr6tsglijo0osmk48d15` (`siteId`),
  KEY `FK_8hvum7fp1pllh4ckbjq2o1phc` (`statusId`),
  KEY `FK_eau65f34mjxbh5skjuisl3ehs` (`ticketType`),
  CONSTRAINT `FK_3pr4eim8lxc1shpm7x3lmf8dk` FOREIGN KEY (`priority`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_7w6cck3isi9w76nxvsjrinmq0` FOREIGN KEY (`assetId`) REFERENCES `asset` (`assetId`),
  CONSTRAINT `FK_8hvum7fp1pllh4ckbjq2o1phc` FOREIGN KEY (`statusId`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_eau65f34mjxbh5skjuisl3ehs` FOREIGN KEY (`ticketType`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_h52mjcr6tsglijo0osmk48d15` FOREIGN KEY (`siteId`) REFERENCES `site` (`siteId`),
  CONSTRAINT `FK_jfvr399866v36w84303rm7s3u` FOREIGN KEY (`severity`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_kq2qinnrd2os4pmvb7aiwo357` FOREIGN KEY (`serviceTypeId`) REFERENCES `service_type` (`serviceTypeId`),
  CONSTRAINT `FK_lnmo91jy7a1ilx8kiybj19pfa` FOREIGN KEY (`reporterUserId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_nu24hqr1jox6dn9k094is921u` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_oess3lv382smng08jufn7em6m` FOREIGN KEY (`issueTypeId`) REFERENCES `issue_type` (`issueTypeId`),
  CONSTRAINT `FK_peam4jyjf9fcveqh1u3aw37vg` FOREIGN KEY (`resolverUserId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_s3m689xufr99ta919k0mirgvq` FOREIGN KEY (`createdBy`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticket_archive`
--

DROP TABLE IF EXISTS `ticket_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket_archive` (
  `ticketArchiveId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `description` text,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `relatedArchiveId` int(11) DEFAULT NULL,
  `ticketId` varchar(255) DEFAULT NULL,
  `ticketMasterId` varchar(255) DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `reporterUserId` varchar(255) DEFAULT NULL,
  `resolverUserId` varchar(255) DEFAULT NULL,
  `statusId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ticketArchiveId`),
  KEY `FK_dcbiyoveecp1nonflowfffo21` (`modifiedBy`),
  KEY `FK_bjy6xtu2369egiu9cav4aoj4o` (`reporterUserId`),
  KEY `FK_4jc28ku9yq01w7fno1yig7nye` (`resolverUserId`),
  KEY `FK_2pyoqadubbd50r6ktfmk7pdb1` (`statusId`),
  CONSTRAINT `FK_2pyoqadubbd50r6ktfmk7pdb1` FOREIGN KEY (`statusId`) REFERENCES `enumeration` (`enumerationId`),
  CONSTRAINT `FK_4jc28ku9yq01w7fno1yig7nye` FOREIGN KEY (`resolverUserId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_bjy6xtu2369egiu9cav4aoj4o` FOREIGN KEY (`reporterUserId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_dcbiyoveecp1nonflowfffo21` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticket_attachment`
--

DROP TABLE IF EXISTS `ticket_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket_attachment` (
  `attachmentId` int(11) NOT NULL AUTO_INCREMENT,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `attachmentName` varchar(255) DEFAULT NULL,
  `attachmentPath` varchar(255) DEFAULT NULL,
  `ticketId` varchar(255) NOT NULL,
  PRIMARY KEY (`attachmentId`),
  KEY `FK_j0gp5cmysvlvblblsxbm7cge6` (`ticketId`),
  CONSTRAINT `FK_j0gp5cmysvlvblblsxbm7cge6` FOREIGN KEY (`ticketId`) REFERENCES `ticket` (`ticketId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticket_ext`
--

DROP TABLE IF EXISTS `ticket_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket_ext` (
  `ticketId` varchar(255) NOT NULL,
  `ticketArchiveId` varchar(255) NOT NULL,
  `fieldName` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `fieldValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ticketId`,`ticketArchiveId`,`fieldName`),
  CONSTRAINT `FK_4ka3q84ykcjs2rshx3xdmt3c6` FOREIGN KEY (`ticketId`) REFERENCES `ticket` (`ticketId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `emailId` varchar(255) NOT NULL,
  `enableNotifications` bit(1) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `organizationId` int(11) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  KEY `FK_aiveopkxvo3ypb36d9l424m4f` (`organizationId`),
  CONSTRAINT `FK_aiveopkxvo3ypb36d9l424m4f` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_login`
--

DROP TABLE IF EXISTS `user_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_login` (
  `userId` varchar(255) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `disabledTimestamp` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `lastLoginTimestamp` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_permission`
--

DROP TABLE IF EXISTS `user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_permission` (
  `user_userId` varchar(255) NOT NULL,
  `permissions_permissionId` varchar(255) NOT NULL,
  KEY `FK_3uhj3herm3yikij6yl0v3ouv` (`permissions_permissionId`),
  KEY `FK_dx4qxp1x3o7ws37nx85nc8040` (`user_userId`),
  CONSTRAINT `FK_3uhj3herm3yikij6yl0v3ouv` FOREIGN KEY (`permissions_permissionId`) REFERENCES `permission` (`permissionId`),
  CONSTRAINT `FK_dx4qxp1x3o7ws37nx85nc8040` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_userId` varchar(255) NOT NULL,
  `roles_roleTypeId` int(11) NOT NULL,
  KEY `FK_pf69iacod2cfptqoyrpp97lyp` (`roles_roleTypeId`),
  KEY `FK_7i6oqaw8vexokkwucrvgf3sfj` (`user_userId`),
  CONSTRAINT `FK_7i6oqaw8vexokkwucrvgf3sfj` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_pf69iacod2cfptqoyrpp97lyp` FOREIGN KEY (`roles_roleTypeId`) REFERENCES `role_type` (`roleTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor_service_asset`
--

DROP TABLE IF EXISTS `vendor_service_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_service_asset` (
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` datetime DEFAULT NULL,
  `userId` varchar(255) NOT NULL,
  `serviceTypeId` int(11) NOT NULL,
  `assetId` int(11) NOT NULL,
  PRIMARY KEY (`userId`,`serviceTypeId`,`assetId`),
  KEY `FK_80wpd9b9rgyksrc3jsooo4st5` (`serviceTypeId`),
  KEY `FK_rviv4mm3am5b5we2y0pmc9iv9` (`assetId`),
  CONSTRAINT `FK_3jyblkqe3x9rcibrjvbtjov5x` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  CONSTRAINT `FK_80wpd9b9rgyksrc3jsooo4st5` FOREIGN KEY (`serviceTypeId`) REFERENCES `service_type` (`serviceTypeId`),
  CONSTRAINT `FK_rviv4mm3am5b5we2y0pmc9iv9` FOREIGN KEY (`assetId`) REFERENCES `asset` (`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-15 16:49:24
