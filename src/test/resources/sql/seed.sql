
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE sales;
TRUNCATE TABLE inventory;
TRUNCATE TABLE plants;
TRUNCATE TABLE categories;
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO categories (id, name, parent_id) VALUES
  (1, 'Indoor', NULL),
  (2, 'Outdoor', NULL);


INSERT INTO categories (id, name, parent_id) VALUES
  (3, 'Succulent', 1),
  (4, 'Flowering', 1),
  (8, 'Climbers', 1),
  (10, 'Ferns', 1),
  (11, 'Cacti', 1);


INSERT INTO categories (id, name, parent_id) VALUES
  (5, 'Herbs', 2),
  (6, 'Trees', 2),
  (7, 'Shrubs', 2),
  (9, 'Aquatic', 2);


INSERT INTO plants (id, name, category_id, price, quantity) VALUES
  (1, 'Monstera', 10, 25.00, 15),
  (2, 'Snake Plant', 3, 18.00, 20),
  (3, 'Pothos', 8, 12.00, 3),
  (4, 'Peace Lily', 4, 22.00, 10),
  (5, 'Spider Plant', 10, 15.00, 25),
  (6, 'Aloe Vera', 3, 10.00, 30),
  (7, 'Rose Bush', 4, 20.00, 18),
  (8, 'Lavender', 5, 8.00, 40),
  (9, 'Basil', 5, 5.00, 2),
  (10, 'Ficus', 6, 35.00, 12),
  (11, 'Jade Plant', 3, 14.00, 22);


INSERT INTO sales (id, plant_id, quantity, total_price, sold_at) VALUES
  (1, 1, 2, 50.00, NOW() - INTERVAL 2 DAY),
  (2, 6, 1, 10.00, NOW() - INTERVAL 1 DAY),
  (3, 9, 3, 15.00, NOW());
