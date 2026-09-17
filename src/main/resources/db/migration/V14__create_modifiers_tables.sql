CREATE TABLE modifier_groups
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(250) NOT NULL,
    required   boolean      NOT NULL DEFAULT FALSE,
    min_select INT          NOT NULL DEFAULT 0,
    max_select INT          NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE modifier_options
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(250)   NOT NULL,
    price_delta       NUMERIC(10, 2) not null,
    is_free           boolean        NOT NULL DEFAULT FALSE,
    modifier_group_id BIGINT         NOT NULL REFERENCES modifier_groups (id),
    created_at        TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE product_modifier_groups
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id        BIGINT     NOT NULL REFERENCES products (id),
    modifier_group_id BIGINT     NOT NULL REFERENCES modifier_groups (id),
<<<<<<< HEAD
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
=======
    created_at        TIMESTAMPZ NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPZ NOT NULL DEFAULT NOW()
>>>>>>> f1e3d73 (Add ModifierGroup, ModifierOption, ProductModifierGroup entities and migration)

);