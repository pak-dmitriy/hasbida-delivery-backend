ALTER TABLE products ADD COLUMN discount_percent INT NOT NULL DEFAULT 0
CHECK (discount_percent BETWEEN 0 AND 100)