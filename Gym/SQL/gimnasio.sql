drop database if exists GYM;
create database GYM;

use GYM;

--
-- Table structure for table `PROFES`
--

DROP TABLE IF EXISTS `PROFES`;
CREATE TABLE `PROFES` (
  `ID_PROFES` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(20) DEFAULT NULL,
  `APELLIDO` varchar(20) DEFAULT NULL,
  `DNI` varchar(12) DEFAULT NULL,
  `FECHA_NAC` date DEFAULT NULL,
  `DIR` varchar(30) DEFAULT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `SEXO` char(1) DEFAULT NULL,
  `OBSERV` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`ID_PROFES`)
);

DROP TABLE IF EXISTS `CLASE`;
CREATE TABLE `GYM`.`CLASE` (
  `ID_CLASE` int(11) NOT NULL AUTO_INCREMENT,
  `ID_PROFES` int(11) NOT NULL,
  `FECHA` date DEFAULT NULL,
  `ACTIV` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID_CLASE`),
  KEY `FK_CLASE` (`ID_PROFES`),
  CONSTRAINT `FK_CLASE` FOREIGN KEY (`ID_PROFES`) REFERENCES `PROFES` (`ID_PROFES`) ON DELETE CASCADE
);








--
-- Table structure for table `USUARIOS`
--

DROP TABLE IF EXISTS `USUARIOS`;
CREATE TABLE `USUARIOS` (
  `USUARIO` varchar(20) DEFAULT NULL,
  `PASSWD` varchar(20) DEFAULT NULL,
  `ADMINIS` char(1) DEFAULT NULL
) ;

--
-- Table structure for table `arancels`
--

DROP TABLE IF EXISTS `arancels`;
CREATE TABLE `arancels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) DEFAULT NULL,
  `precio` float DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `activo` int(5) DEFAULT NULL,
  `categoria` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
);








--
-- Table structure for table `socioarancels`
--

DROP TABLE IF EXISTS `socioarancels`;
CREATE TABLE `socioarancels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_socio` int(11) DEFAULT NULL,
  `id_arancel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);



--
-- Table structure for table `socios`
--

DROP TABLE IF EXISTS `socios`;
CREATE TABLE `socios` (
  `ID_DATOS_PERS` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(20) DEFAULT NULL,
  `APELLIDO` varchar(20) DEFAULT NULL,
  `DNI` varchar(12) DEFAULT NULL,
  `FECHA_NAC` varchar(10) DEFAULT NULL,
  `FECHA_ING` date DEFAULT NULL,
  `DIR` varchar(30) DEFAULT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `SEXO` char(1) DEFAULT NULL,
  `FECHA_ULT_PAGO` date DEFAULT NULL,
  `ACTIV` bigint(20) DEFAULT NULL,
  `FECHA_PROX_PAGO` date DEFAULT NULL,
  `ACTIVO` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID_DATOS_PERS`)
);

--
-- Table structure for table `FICHA_MEDICA`
--
--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
CREATE TABLE `pagos` (
  `ID_PAGOS` int(11) NOT NULL AUTO_INCREMENT,
  `ID_DATOS_PERS` int(11) NOT NULL,
  `FECHA` date DEFAULT NULL,
  `MONTO` float DEFAULT NULL,
  PRIMARY KEY (`ID_PAGOS`),
  KEY `FK_PAGOS` (`ID_DATOS_PERS`),
  CONSTRAINT `FK_PAGOS` FOREIGN KEY (`ID_DATOS_PERS`) REFERENCES `socios` (`ID_DATOS_PERS`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `fichas`;

CREATE TABLE `fichas` (
  `ID_FICHA_MEDICA` int(11) NOT NULL AUTO_INCREMENT,
  `ID_DATOS_PERS` int(11) NOT NULL,
  `TEL_EMERG` varchar(20) DEFAULT NULL,
  `GRUPO_SANG` varchar(2) DEFAULT NULL,
  `FACTOR` varchar(3) DEFAULT NULL,
  `ALERGICO` varchar(30) DEFAULT NULL,
  `MEDICAM` varchar(30) DEFAULT NULL,
  `OBSERV` varchar(3000) DEFAULT NULL,
  `PATOLOGIAS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_FICHA_MEDICA`),
  KEY `FK_FICHA_MEDICA` (`ID_DATOS_PERS`),
  CONSTRAINT `FK_FICHA_MEDICA` FOREIGN KEY (`ID_DATOS_PERS`) REFERENCES `socios` (`ID_DATOS_PERS`) ON DELETE CASCADE
);
--
-- Table structure for table `asistencias`
--

DROP TABLE IF EXISTS `asistencias`;
CREATE TABLE `asistencias` (
  `ID_ASISTENCIA` int(11) NOT NULL AUTO_INCREMENT,
  `ID_DATOS_PERS` int(11) NOT NULL,
  `FECHA` date DEFAULT NULL,
  `ACTIV` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_ASISTENCIA`),
  KEY `FK_ASISTENCIA` (`ID_DATOS_PERS`),
  CONSTRAINT `FK_ASISTENCIA` FOREIGN KEY (`ID_DATOS_PERS`) REFERENCES `socios` (`ID_DATOS_PERS`)
);

CREATE TABLE `GYM`.`huellas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `huella` BLOB NULL,
  `dedo` VARCHAR(45) NULL,
  `client_id` INT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `gym`.`combos` (
  `id_combo` INT NOT NULL,
  `id_activ` INT NULL,
  `dias` INT NULL,
  PRIMARY KEY (`id_combo`));
ALTER TABLE `gym`.`combos` 
CHANGE COLUMN `id_activ` `id_activ` INT(11) NOT NULL ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id_combo`, `id_activ`);


ALTER TABLE `gym`.`asistencias` 
CHANGE COLUMN `ACTIV` `ID_ACTIV` INT(11) NULL DEFAULT NULL ,
ADD COLUMN `ID_ACTIV_COMBO` INT(11) NULL AFTER `ID_ACTIV`;

ALTER TABLE `gym`.`arancels` 
ADD COLUMN `dias` INT(2) NULL DEFAULT 0 AFTER `categoria`;




ALTER TABLE `gym`.`asistencias` 
DROP FOREIGN KEY `FK_ASISTENCIA`;
ALTER TABLE `gym`.`asistencias` 
ADD CONSTRAINT `FK_ASISTENCIA`
  FOREIGN KEY (`ID_DATOS_PERS`)
  REFERENCES `gym`.`socios` (`ID_DATOS_PERS`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;


ALTER TABLE `gym`.`huellas` 
ADD CONSTRAINT `fk_cliente`
  FOREIGN KEY (`client_id`)
  REFERENCES `gym`.`socios` (`ID_DATOS_PERS`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;



ALTER TABLE `gym`.`socioarancels` 
ADD CONSTRAINT `fk_cliente_arancel`
  FOREIGN KEY (`id_socio`)
  REFERENCES `gym`.`socios` (`ID_DATOS_PERS`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_arancel`
  FOREIGN KEY (`id_arancel`)
  REFERENCES `gym`.`arancels` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


ALTER TABLE `gym`.`combos` 
ADD CONSTRAINT `fk_combo`
  FOREIGN KEY (`id_combo`)
  REFERENCES `gym`.`arancels` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_activ_combo`
  FOREIGN KEY (`id_activ`)
  REFERENCES `gym`.`arancels` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
