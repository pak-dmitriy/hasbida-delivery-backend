ALTER TABLE order_items ADD COLUMN discount_percent INT NOT NULL DEFAULT 0
    CHECK (discount_percent BETWEEN 0 AND 100);
ALTER TABLE order_items ADD COLUMN discount_amount NUMERIC(10, 2) NOT NULL DEFAULT 0
CHECK (discount_amount >= 0);
