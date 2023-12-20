-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema too_many_cookbooks
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema too_many_cookbooks
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `too_many_cookbooks` DEFAULT CHARACTER SET utf8 ;
USE `too_many_cookbooks` ;

-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`ingredient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`ingredient` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`recipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`recipe` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `image` MEDIUMBLOB NULL DEFAULT NULL,
  `prep_time` FLOAT NULL DEFAULT NULL,
  `servings` INT NULL DEFAULT NULL,
  `instructions` TEXT NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`cookbook`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`cookbook` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `image` MEDIUMBLOB NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`ingredient_recipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`ingredient_recipe` (
  `ingredient_id` BIGINT NOT NULL,
  `recipe_id` BIGINT NOT NULL,
  `amount` FLOAT NOT NULL,
  `unit` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`ingredient_id`, `recipe_id`),
  INDEX `fk_ingredients_has_recipe_recipe1_idx` (`recipe_id` ASC) VISIBLE,
  INDEX `fk_ingredients_has_recipe_ingredients_idx` (`ingredient_id` ASC) VISIBLE,
  CONSTRAINT `fk_ingredients_has_recipe_ingredients`
    FOREIGN KEY (`ingredient_id`)
    REFERENCES `too_many_cookbooks`.`ingredient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ingredients_has_recipe_recipe1`
    FOREIGN KEY (`recipe_id`)
    REFERENCES `too_many_cookbooks`.`recipe` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`shopping_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`shopping_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ingredient_id` BIGINT NOT NULL,
  `amount` FLOAT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_shopping_list_ingredient1_idx` (`ingredient_id` ASC) VISIBLE,
  CONSTRAINT `fk_shopping_list_ingredient1`
    FOREIGN KEY (`ingredient_id`)
    REFERENCES `too_many_cookbooks`.`ingredient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `too_many_cookbooks`.`recipe_cookbook`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`recipe_cookbook` (
  `recipe_id` BIGINT NOT NULL,
  `cookbook_id` BIGINT NOT NULL,
  PRIMARY KEY (`recipe_id`, `cookbook_id`),
  INDEX `fk_recipe_has_cookbooks_cookbooks1_idx` (`cookbook_id` ASC) VISIBLE,
  INDEX `fk_recipe_has_cookbooks_recipe1_idx` (`recipe_id` ASC) VISIBLE,
  CONSTRAINT `fk_recipe_has_cookbooks_recipe1`
    FOREIGN KEY (`recipe_id`)
    REFERENCES `too_many_cookbooks`.`recipe` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_recipe_has_cookbooks_cookbooks1`
    FOREIGN KEY (`cookbook_id`)
    REFERENCES `too_many_cookbooks`.`cookbook` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
