CREATE TABLE order_items
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id    BIGINT REFERENCES products (id),
    order_id      BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_name  VARCHAR(50)    NOT NULL,
    product_price NUMERIC(10, 2) NOT NULL,
    quantity      INT            NOT NULL CHECK (quantity > 0),
    subtotal      NUMERIC(10, 2) NOT NULL,
    created_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW()
)