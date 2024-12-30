INSERT INTO clients(id, name, surname, age, cash)
VALUES (1, 'Jan', 'Kos', 18, 2000),
       (2, 'Stefan', 'Ziemniak', 22, 5000),
       (3, 'Taduesz', 'Filak', 33, 3400),
       (4, 'Kazik', 'Milosz', 35, 8000),
       (5, 'Michal', 'Detka', 48, 1000),
       (6, 'Stefan', 'Mech', 22, 3400),
       (7, 'Stachu', 'Kowal', 66, 10000),
       (8, 'Agata', 'Mrowiec', 26, 9000),
       (9, 'Darek', 'Marjankowski', 58, 3000);

INSERT INTO products(id, name, category, price)
VALUES (1, 'camera', 'electronics', 1400),
       (2, 'pan tadeusz', 'book', 120),
       (3, 'scooter', 'automotive', 8050),
       (4, 'chicken', 'groceries', 29),
       (5, 'beans', 'groceries', 13),
       (6, 'pepper', 'groceries', 10),
       (7, 'cream', 'cosmetics', 18),
       (8, 'flight', 'book', 130),
       (9, 'tires', 'automotive', 1854),
       (10, 'paper', 'office supplies', 30),
       (11, 'powder', 'cosmetics', 38),
       (12, 'shoes', 'clothing', 290),
       (13, 'monitor', 'electronics', 800),
       (14, 'keyboard', 'electronics', 90),
       (15, 'potato', 'groceries', 10),
       (16, 'flour', 'groceries', 8),
       (17, 'coffee', 'groceries', 12),
       (18, 'table', 'home', 950),
       (19, 'socks', 'clothing', 30),
       (20, 'underwear', 'clothing', 35),
       (21, 'buns', 'groceries', 3),
       (22, 'glass', 'home', 19),
       (23, 'windows', 'home', 5400),
       (24, 'desk', 'home', 300),
       (25, 'phone', 'electronics', 1200),
       (26, 't-shirt', 'clothing', 86),
       (27, 'computer', 'electronics', 2400),
       (28, 'bread', 'groceries', 24),
       (29, 'car', 'automotive', 24000),
       (30, 'alternator', 'automotive', 2400);

INSERT INTO orders(id, client_id, product_id)
values (1,1,2),
       (2,1,2),
       (3,1,4),
       (4,1,10),
       (5,1,10),
       (6,2,3),
       (7,2,15),
       (8,2,15),
       (9,2,15),
       (10,2,20),
       (11,3,1),
       (12,3,1),
       (13,3,2),
       (14,3,20),
       (15,3,22),
       (16,3,23),
       (17,3,24),
       (18,1,2),
       (19,1,2),
       (20,1,2),
       (21,1,2),
       (22,1,29),
       (23,1,30),
       (24,1,24),
       (25,4,3),
       (26,4,3),
       (27,4,25),
       (28,4,18),
       (29,4,18),
       (30,5,4),
       (31,5,4),
       (32,5,5),
       (33,5,13),
       (34,5,14),
       (35,9,1),
       (36,9,1),
       (37,9,11),
       (38,9,12),
       (39,9,27),
       (40,9,26);


