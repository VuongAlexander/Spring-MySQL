CREATE DATABASE IF NOT EXISTS `testdb`;

USE `testdb`;

DROP TABLE IF EXISTS `books`;

CREATE TABLE `books` (
    `id`                INTEGER PRIMARY KEY,
    `isbn`              varchar(255) NOT NULL,
    `title`             varchar(255) NOT NULL,
    `first_name`        varchar(255) NOT NULL,
    `last_name`         varchar(255) NOT NULL,
    `description`       varchar(255) NOT NULL,
    `published`         INT DEFAULT 0
);
