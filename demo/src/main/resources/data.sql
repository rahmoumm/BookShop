INSERT INTO Book (book_id, name, rating) VALUES (11, 'gambler', 4.5);
INSERT INTO Book (book_id, name, rating) VALUES (12, 'hz', 3.5);
INSERT INTO Book (book_id, name, rating) VALUES (13, 'gamazvbler', 1.9);

-- password est abc
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password)
    VALUES (11, 'Mourad', 'Rahmouoni', 'm@gmail.com','$2y$10$/xdmnLZAh1IW192a1CyWO.HWq4OAZQIv6UNxk1sSkZkNEk/9uTCVW');
--password est def
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password)
    VALUES (12, 'Karim', 'Rahmouoni', 'k@gmail.com','$2y$10$kcIIpdu3O8q2JAL6jXSZb.byOz4y/Tmdq28456yulnsPa7fo.78wm');
-- password est ghi
INSERT INTO MY_USERS (user_id, first_name, last_name, email, password)
    VALUES (13, 'Hamid', 'Rahmouoni', 'h@gmail.com','$2y$10$JwJSxuk06GFJo7v5lmmq4ONdYRhrodVhjVZbbtXp1d/Q23ZBkq9He');

INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(11, 11, 10, 15.0);
INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(11, 12, 15, 25.99);
INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(11, 13, 20, 9.99);
INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(12, 11, 30, 15.50);
INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(12, 12, 35, 25.0);
INSERT INTO STOCK (user_id, book_id, available_quantity, price) VALUES(13, 12, 40, 25.99);

INSERT INTO ROLE(role_id, role_name) VALUES(1, 'ADMIN');
INSERT INTO ROLE(role_id, role_name) VALUES(2, 'SELLER');
INSERT INTO ROLE(role_id, role_name) VALUES(3, 'USER');

INSERT INTO USERS_ROLE(user_id, role_id) VALUES(11, 1);
INSERT INTO USERS_ROLE(user_id, role_id) VALUES(11, 2);
INSERT INTO USERS_ROLE(user_id, role_id) VALUES(12, 2);


INSERT INTO BASKET(basket_id, user_id, total_amount) VALUES(11, 11, 110.0);
INSERT INTO BASKET(basket_id, user_id, total_amount) VALUES(12, 12, 120.0);

INSERT INTO BOOKS_IN_BASKET(basket_id, book_id) VALUES (11, 11);
INSERT INTO BOOKS_IN_BASKET(basket_id, book_id) VALUES (11, 12);