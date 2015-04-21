CREATE TABLE IF NOT EXISTS movies (ID identity, name varchar(255), genre varchar(255));
merge into movies(name, genre) key(name) values ('DEMO - step brothers', 'comedy');
merge into movies(name, genre) key(name) values ('DEMO - diehard', 'action');