CREATE TABLE order_item_modifiers
(
    id                 BIGINT         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    modifier_option_id BIGINT         NOT NULL REFERENCES modifier_options (id),
    order_item_id      BIGINT         NOT NULL REFERENCES order_items (id) ON DELETE CASCADE,
    option_name        VARCHAR(50)    NOT NULL,
    price_delta        NUMERIC(10, 2) NOT NULL,
    created_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);