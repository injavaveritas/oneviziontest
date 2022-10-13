CREATE SCHEMA IF NOT EXISTS one_vizion;

CREATE TABLE IF NOT EXISTS one_vizion.book (
   id SERIAL8 NOT NULL,
   title VARCHAR(150) NOT NULL,
   author VARCHAR(150) NOT NULL,
   description VARCHAR(150),
   PRIMARY KEY (id)
);