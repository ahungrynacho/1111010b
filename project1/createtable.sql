-- Brian Huynh 57641580

CREATE DATABASE IF NOT EXISTS `moviedb`;
USE `moviedb`;

-- Drop tables in the reverse order of creation.
/*
DROP TABLE IF EXISTS `sales`;
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `stars_in_movies`;
DROP TABLE IF EXISTS `stars`;
DROP TABLE IF EXISTS `genres_in_movies`;
DROP TABLE IF EXISTS `genres`;
DROP TABLE IF EXISTS `movies`;
DROP TABLE IF EXISTS `creditcards`;
*/

CREATE TABLE IF NOT EXISTS `creditcards` (
	`id` VARCHAR(20) NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `expiration` DATE NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `movies` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(100) NOT NULL,
    `year` INTEGER NOT NULL,
    `director` VARCHAR(100) NOT NULL,
    `banner_url` VARCHAR(200) DEFAULT '',
    `trailer_url` VARCHAR(200) DEFAULT '',
    PRIMARY KEY(`id`)

);

CREATE TABLE IF NOT EXISTS `genres` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `genres_in_movies` (
	`genre_id` INTEGER NOT NULL,
    `movie_id` INTEGER NOT NULL,
    FOREIGN KEY(`genre_id`) REFERENCES `genres`(`id`),
	FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`)
);

CREATE TABLE IF NOT EXISTS `stars` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `dob` DATE DEFAULT NULL,
    `photo_url` VARCHAR(200) DEFAULT '',
    PRIMARY KEY(`id`)
);
CREATE TABLE IF NOT EXISTS `stars_in_movies` (
	`star_id` INTEGER NOT NULL,
    `movie_id` INTEGER NOT NULL,
    FOREIGN KEY(`star_id`) REFERENCES `stars`(`id`),
    FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`)
);


CREATE TABLE IF NOT EXISTS `customers` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `cc_id` VARCHAR(20) NOT NULL,
    `address` VARCHAR(200) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `password` VARCHAR(20) NOT NULL,
    FOREIGN KEY(`cc_id`) REFERENCES `creditcards`(`id`),
    PRIMARY KEY(`id`)
    
);

CREATE TABLE IF NOT EXISTS `sales` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `customer_id` INTEGER NOT NULL,
    `movie_id` INTEGER NOT NULL,
    `sale_date` DATE NOT NULL,
    FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`),
    FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`),
    PRIMARY KEY(`id`)
    
);

SELECT *
FROM creditcards cc
WHERE cc.id = 0;


