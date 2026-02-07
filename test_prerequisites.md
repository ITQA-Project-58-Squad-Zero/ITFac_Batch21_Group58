# Test Data Prerequisites & Database State Requirements

## Quick Checklist

- [ ] Application running at `http://localhost:8080`
- [ ] Admin (`admin`/`admin123`) and User (`testuser`/`test123`) accounts exist
- [ ] At least 11 plants and 11 categories exist (for pagination)
- [ ] At least 1 sale record exists (for sales page tests)

---

## 1. Categories (3-10 chars)

| ID  | Name        | Parent  | Notes               |
| --- | ----------- | ------- | ------------------- |
| 1   | `Indoor`    | (none)  | Parent only         |
| 2   | `Outdoor`   | (none)  | Parent only         |
| 3   | `Succulent` | Indoor  | TC_PL_UI_004 filter |
| 4   | `Flowering` | Indoor  |                     |
| 5   | `Herbs`     | Outdoor |                     |
| 6   | `Trees`     | Outdoor |                     |
| 7   | `Shrubs`    | Outdoor |                     |
| 8   | `Climbers`  | Indoor  |                     |
| 9   | `Aquatic`   | Outdoor |                     |
| 10  | `Ferns`     | Indoor  |                     |
| 11  | `Cacti`     | Indoor  |                     |

---

## 2. Plants (subcategories only)

| ID  | Name           | Category  | Price | Stock | Notes             |
| --- | -------------- | --------- | ----- | ----- | ----------------- |
| 1   | `Monstera`     | Ferns     | 25.00 | 15    | Search test       |
| 2   | `Snake Plant`  | Succulent | 18.00 | 20    | Multi-word search |
| 3   | `Pothos`       | Climbers  | 12.00 | 3     | Low stock         |
| 4   | `Peace Lily`   | Flowering | 22.00 | 10    |                   |
| 5   | `Spider Plant` | Ferns     | 15.00 | 25    |                   |
| 6   | `Aloe Vera`    | Succulent | 10.00 | 30    | Filter test       |
| 7   | `Rose Bush`    | Flowering | 20.00 | 18    |                   |
| 8   | `Lavender`     | Herbs     | 8.00  | 40    |                   |
| 9   | `Basil`        | Herbs     | 5.00  | 2     | Low stock         |
| 10  | `Ficus`        | Trees     | 35.00 | 12    |                   |
| 11  | `Jade Plant`   | Succulent | 14.00 | 22    | Filter test       |

---

## 3. Sales Records

| ID  | Plant     | Quantity | Total Price | Notes                      |
| --- | --------- | -------- | ----------- | -------------------------- |
| 1   | Monstera  | 2        | 50.00       | SM_UI_A_01 view list       |
| 2   | Aloe Vera | 1        | 10.00       | SM_UI_U_03 delete hidden   |
| 3   | Basil     | 3        | 15.00       | Multiple records for table |

> **Note**: SM_UI_A_04 will **CREATE** a new sale during test execution

---

## 4. Database Setup SQL

```sql
-- Clear data
DELETE FROM sales;
DELETE FROM plants;
DELETE FROM categories WHERE name = 'Tropical';

-- Parent categories
INSERT INTO categories (id, name, parent_id) VALUES
  (1, 'Indoor', NULL),
  (2, 'Outdoor', NULL);

-- Subcategories under Indoor
INSERT INTO categories (id, name, parent_id) VALUES
  (3, 'Succulent', 1),
  (4, 'Flowering', 1),
  (8, 'Climbers', 1),
  (10, 'Ferns', 1),
  (11, 'Cacti', 1);

-- Subcategories under Outdoor
INSERT INTO categories (id, name, parent_id) VALUES
  (5, 'Herbs', 2),
  (6, 'Trees', 2),
  (7, 'Shrubs', 2),
  (9, 'Aquatic', 2);

-- Plants
INSERT INTO plants (id, name, category_id, price, stock) VALUES
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

-- Sales records
INSERT INTO sales (id, plant_id, quantity, total_price, sold_at) VALUES
  (1, 1, 2, 50.00, NOW() - INTERVAL 2 DAY),
  (2, 6, 1, 10.00, NOW() - INTERVAL 1 DAY),
  (3, 9, 3, 15.00, NOW());
```

---

## 5. Test Data Actions Summary

| Feature               | Test          | Action                    |
| --------------------- | ------------- | ------------------------- |
| `view_sales.feature`  | SM_UI_A_01    | READ sales table          |
| `view_sales.feature`  | SM_UI_A_03    | READ, opens Sell form     |
| `view_sales.feature`  | SM_UI_U_01-04 | READ, verify restrictions |
| `create_sale.feature` | SM_UI_A_04    | **CREATE** new sale       |
| `create_sale.feature` | SM_UI_A_05    | VALIDATE form (no create) |
