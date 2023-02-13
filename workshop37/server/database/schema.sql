DROP SCHEMA IF EXISTS feeds;
CREATE SCHEMA feeds;

USE feeds;

DROP TABLE IF EXISTS posts;
CREATE TABLE posts (

    post_id VARCHAR(8) NOT NULL,
    comments MEDIUMTEXT NOT NULL,
    picture MEDIUMBLOB NOT NULL,

    PRIMARY KEY(post_id)
);