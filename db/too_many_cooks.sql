-- MySQL Workbench Synchronization
-- Generated: 2023-12-10 22:20
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: perze

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER SCHEMA `too_many_cookbooks`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;

ALTER TABLE `too_many_cookbooks`.`shopping_item` 
DROP FOREIGN KEY `fk_shopping_list_ingredient1`;

ALTER TABLE `too_many_cookbooks`.`ingredient` 
CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `too_many_cookbooks`.`recipe` 
CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `too_many_cookbooks`.`cookbook` 
CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`ingredient_recipe` (
  `ingredient_id` BIGINT(20) NOT NULL,
  `recipe_id` BIGINT(20) NOT NULL,
  `amount` INT(11) NOT NULL,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `too_many_cookbooks`.`shopping_item` 
CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `too_many_cookbooks`.`recipe_cookbook` (
  `recipe_id` BIGINT(20) NOT NULL,
  `cookbook_id` BIGINT(20) NOT NULL,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `too_many_cookbooks`.`shopping_item` 
ADD CONSTRAINT `fk_shopping_list_ingredient1`
  FOREIGN KEY (`ingredient_id`)
  REFERENCES `too_many_cookbooks`.`ingredient` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
