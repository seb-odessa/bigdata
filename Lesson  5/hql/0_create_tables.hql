DROP TABLE IF EXISTS users  PURGE;
DROP TABLE IF EXISTS phones PURGE;
DROP TABLE IF EXISTS rooms  PURGE;
CREATE TABLE users  (id int, name string);
CREATE TABLE phones (id int, user_id int, phone_number int);
CREATE TABLE rooms  (id int, phone_id int, room_number int);
SHOW TABLES;

LOAD DATA INPATH '/tmp/users'  OVERWRITE INTO TABLE users;
LOAD DATA INPATH '/tmp/phones' OVERWRITE INTO TABLE phones;
LOAD DATA INPATH '/tmp/rooms'  OVERWRITE INTO TABLE rooms;
SELECT * FROM users;
SELECT * FROM phones;
SELECT * FROM rooms;

