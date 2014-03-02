create database gimnasio;

CREATE TABLE `gimnasio`.`huellas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `huella` BLOB NULL,
  `dedo` VARCHAR(45) NULL,
  `client_id` INT NULL,
  PRIMARY KEY (`id`));
