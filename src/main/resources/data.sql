-- Admin kullanıcısı
INSERT INTO admins (username, password, active) 
VALUES ('admin', '$2a$10$HRHi6.62M/Gcl9Vy.0tlU.jL1ZXwSs0gYJ.iYXNzxaGGBOHU.OHWi', true);

-- Müşteriler
-- Şifre: müşteri123 (BCrypt ile şifrelenmiş)
INSERT INTO customers (username, password, try_balance, try_usable_balance, active) 
VALUES ('musteri1', '$2a$10$A9VrDxuBvl3fVZ3SbkRiOuOGwOpkKJtO0dwOJfJ5XxBXKNtZHYN7a', 10000.0, 10000.0, true);

INSERT INTO customers (username, password, try_balance, try_usable_balance, active) 
VALUES ('musteri2', '$2a$10$A9VrDxuBvl3fVZ3SbkRiOuOGwOpkKJtO0dwOJfJ5XxBXKNtZHYN7a', 20000.0, 20000.0, true);

INSERT INTO customers (username, password, try_balance, try_usable_balance, active) 
VALUES ('musteri3', '$2a$10$A9VrDxuBvl3fVZ3SbkRiOuOGwOpkKJtO0dwOJfJ5XxBXKNtZHYN7a', 30000.0, 30000.0, true);

-- Varlıklar
INSERT INTO assets (asset_name, total_supply, price, active) 
VALUES ('Bitcoin', 21000000.0, 60000.0, true);

INSERT INTO assets (asset_name, total_supply, price, active) 
VALUES ('Ethereum', 120000000.0, 2500.0, true);

INSERT INTO assets (asset_name, total_supply, price, active) 
VALUES ('Ripple', 100000000000.0, 0.5, true);

-- Müşteri varlıkları
-- müşteri1 için varlıklar
INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (1, 1, 1.0, 1.0, true);

INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (1, 2, 10.0, 10.0, true);

-- müşteri2 için varlıklar
INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (2, 2, 20.0, 20.0, true);

INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (2, 3, 10000.0, 10000.0, true);

-- müşteri3 için varlıklar
INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (3, 1, 2.0, 2.0, true);

INSERT INTO customer_assets (customer_id, asset_id, size, usable_size, active) 
VALUES (3, 3, 5000.0, 5000.0, true); 