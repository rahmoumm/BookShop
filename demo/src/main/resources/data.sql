INSERT INTO Book (book_id, name, rating) VALUES (11, 'gambler', 4.5);
INSERT INTO Book (book_id, name, rating) VALUES (12, 'hz', 3.5);
INSERT INTO Book (book_id, name, rating) VALUES (13, 'gamazvbler', 1.9);

-- password est abc
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (11, 'Mourad', 'Rahmouoni', 'm@gmail.com','$2y$10$/xdmnLZAh1IW192a1CyWO.HWq4OAZQIv6UNxk1sSkZkNEk/9uTCVW');
--password est def
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (12, 'Karim', 'Rahmouoni', 'k@gmail.com','$2y$10$kcIIpdu3O8q2JAL6jXSZb.byOz4y/Tmdq28456yulnsPa7fo.78wm');
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (13, 'Hamid', 'Rahmouoni', 'h@gmail.com','123');

INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 11, 10);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 12, 15);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 13, 20);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(12, 11, 30);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(12, 12, 35);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(13, 12, 40);

INSERT INTO ROLE(role_id, role_name) VALUES(1, 'ADMIN');
INSERT INTO ROLE(role_id, role_name) VALUES(2, 'OWNER');
INSERT INTO ROLE(role_id, role_name) VALUES(3, 'USER');

INSERT INTO USERS_ROLE(user_id, role_id) VALUES(11, 1);
INSERT INTO USERS_ROLE(user_id, role_id) VALUES(12, 2);
