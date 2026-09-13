create table products
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(50)  not null unique,
    description       VARCHAR(255) not null,
    price             NUMERIC(10, 2) not null,
    stock             int  not null,
    low_stock_threshold int  not null,
    status varchar check (status in ('AVAILABLE', 'PAUSED')) not null,
    max_quantity int not null,
    min_quantity     int not null,
    store_id BIGINT NOT NULL REFERENCES store(id),
    category_id BIGINT NOT NULL REFERENCES category(id),
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    unique (store_id, name)
);