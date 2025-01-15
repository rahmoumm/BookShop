DROP TABLE IF EXISTS MY_USERS cascade;
CREATE TABLE MY_USERS(
     user_id int PRIMARY KEY,
     first_name VARCHAR,
     last_name VARCHAR,
     email VARCHAR,
     password VARCHAR,
     role VARCHAR,
     image BINARY LARGE OBJECT
);

DROP TABLE IF EXISTS BOOK cascade;
CREATE TABLE Book(
     book_id int PRIMARY KEY,
     name VARCHAR,
     rating DOUBLE,
     user_book_id int
--     FOREIGN KEY(user_book_id) REFERENCES MY_USERS(user_id)
);

-- The on delete cascade is important, so that when we delete a record of a book that is referenced
-- it stock will also be deleted, as it is referenced as a foreign key
-- Same thing goes with user id
DROP TABLE IF EXISTS STOCK CASCADE;
CREATE TABLE STOCK(
    book_id int,
    user_id int,
    available_quantity int,
    price DOUBLE,
    FOREIGN KEY (book_id) REFERENCES BOOK(book_id)  ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES MY_USERS(user_id)  ON DELETE CASCADE
);

DROP TABLE IF EXISTS ROLE CASCADE;
CREATE TABLE ROLE(
    role_id int,
    role_name VARCHAR
);

DROP TABLE IF EXISTS BASKET CASCADE;
CREATE TABLE BASKET(
    basket_id int,
    user_id int,
    total_amount double
);