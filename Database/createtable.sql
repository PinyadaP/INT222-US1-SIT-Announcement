
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dbproject221
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dbproject221
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dbproject221` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `dbproject221` ;

-- -----------------------------------------------------
-- Table `dbproject221`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbproject221`.`categories` (
  `categoryId` INT NOT NULL ,
  `categoryName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`categoryId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dbproject221`.`announces`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbproject221`.`announces` (
  `announcementId` INT NOT NULL AUTO_INCREMENT,
  `announcementTitle` VARCHAR(200) NOT NULL,
  `announcementDescription` VARCHAR(10000) NOT NULL,
  `publishDate` DATETIME NULL DEFAULT NULL,
  `closeDate` DATETIME NULL DEFAULT NULL,
  `announcementDisplay` ENUM('Y', 'N') NOT NULL,
  `categories_categoryId` INT NOT NULL,
  PRIMARY KEY (`announcementId`),
  INDEX `fk_announces_categories_idx` (`categories_categoryId` ASC) VISIBLE,
  CONSTRAINT `fk_announces_categories`
    FOREIGN KEY (`categories_categoryId`)
    REFERENCES `dbproject221`.`categories` (`categoryId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
ALTER TABLE `announces` AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `dbproject221`.`users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `email` VARCHAR(150) NULL DEFAULT NULL,
  `role` VARCHAR(45) NULL DEFAULT 'announcer',
  `createdOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB

AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;