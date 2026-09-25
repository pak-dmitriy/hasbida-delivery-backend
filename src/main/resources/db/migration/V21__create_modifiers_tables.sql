CREATE TABLE modifier_groups
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(250) NOT NULL,
    required   boolean      NOT NULL DEFAULT FALSE,
    min_select INT          NOT NULL DEFAULT 0,
    max_select INT          NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    store_id   BIGINT       NOT NULL REFERENCES store (id)
);

CREATE INDEX idx_modifier_groups_store_id ON modifier_groups (store_id);

CREATE TABLE modifier_options
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(250)   NOT NULL,
    price_delta       NUMERIC(10, 2) not null,
    is_free           boolean        NOT NULL DEFAULT FALSE,
    modifier_group_id BIGINT         NOT NULL REFERENCES modifier_groups (id) ON DELETE CASCADE,
    created_at        TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE product_modifier_groups
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id        BIGINT      NOT NULL REFERENCES products (id),
    modifier_group_id BIGINT      NOT NULL REFERENCES modifier_groups (id) ON DELETE CASCADE,
    UNIQUE (product_id, modifier_group_id),

    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);