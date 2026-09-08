CREATE TABLE category
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(50)  NOT NULL,
    description    VARCHAR(50)  NOT NULL,
    category_photo VARCHAR(255) NOT NULL,
    store_id       BIGINT       NOT NULL REFERENCES store (id),
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (store_id, name)
);