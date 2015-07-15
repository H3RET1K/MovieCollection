drop table movies;
CREATE TABLE IF NOT EXISTS movies (ID identity, name varchar(255), genre varchar(255));
drop table users;
CREATE TABLE IF NOT EXISTS users (id identity, username varchar(140), invalidattempts INTEGER, role varchar(140), secretkey varchar(max), islocked INTEGER);
drop table loginhistory;
CREATE TABLE IF NOT EXISTS loginhistory (id identity, username varchar(140), date varchar(140), ipaddress varchar(140), status varchar(140));

