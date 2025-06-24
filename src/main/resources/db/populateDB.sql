DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq_for_meals RESTART WITH 1;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories)
VALUES ('2025-06-24 08:00:00', 'Завтрак: овсянка с фруктами', 350),
       ('2025-06-24 13:00:00', 'Обед: курица с рисом', 600),
       ('2025-06-24 19:30:00', 'Ужин: овощной салат', 400);