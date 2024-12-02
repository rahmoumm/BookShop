INSERT INTO Book (book_id, name, rating, price) VALUES (11, 'gambler', 4.5, 10.99);
INSERT INTO Book (book_id, name, rating, price) VALUES (12, 'hz', 48.5, 4.74);
INSERT INTO Book (book_id, name, rating, price) VALUES (13, 'gamazvbler', 75.9, 0.26);

-- password est abc
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (11, 'Mourad', 'Rahmouoni', 'm@gmail.com','$2y$10$/xdmnLZAh1IW192a1CyWO.HWq4OAZQIv6UNxk1sSkZkNEk/9uTCVW');
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (12, 'Karim', 'Rahmouoni', 'k@gmail.com','def');
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password) VALUES (13, 'Hamid', 'Rahmouoni', 'h@gmail.com','123');

INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 11, 10);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 12, 15);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(11, 13, 20);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(12, 11, 30);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(12, 12, 35);
INSERT INTO STOCK (user_id, book_id, available_quantity) VALUES(13, 12, 40);
