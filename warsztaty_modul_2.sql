create database warsztaty_modul_2
character set utf8mb4
COLLATE utf8mb4_unicode_ci;

create table users (
id BIGINT(20) primary key AUTO_INCREMENT,
email VARCHAR(255) unique,
password varchar(255),
firstName varchar(255),
user_group_id int(11),
FOREIGN KEY(user_group_id) REFERENCES user_group(id)
);

create table user_group(
id int(11) AUTO_INCREMENT primary key,
name VARCHAR(255)
);

create table exercise(
id int(11) AUTO_INCREMENT primary key,
title varchar(255),
description text
);

create table solution(
id int(11) PRIMARY key AUTO_INCREMENT,
created DATETIME,
updated DATETIME,
description TEXT,
exercise_id int(11),
users_id bigint(20),
FOREIGN KEY(exercise_id) REFERENCES exercise(id),
FOREIGN KEY(users_id) references users(id)
);