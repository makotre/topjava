DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2025-06-24 11:00:00', 'Завтрак для юзера', 350, 100000),
       ('2025-06-24 11:00:00', 'Завтрак для админа', 350, 100001),
       ('2025-06-24 13:00:00', 'Обед: курица с рисом', 600, 100000),
       ('2025-06-24 19:30:00', 'Ужин: овощной салат', 400, 100000);