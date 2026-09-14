CREATE TABLE customer
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(50)                                         NOT NULL,
    phone      VARCHAR(20)                                         NOT NULL UNIQUE,
    status     VARCHAR(50) CHECK (status IN ('ACTIVE', 'BLOCKED')) NOT NULL,
    created_at TIMESTAMPTZ                                         NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ                                         NOT NULL DEFAULT NOW()
);

CREATE TABLE customer_address
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    city        VARCHAR(50) NOT NULL,
    street      VARCHAR(50) NOT NULL,
    house       VARCHAR(20) NOT NULL,
    apartment   VARCHAR(20) NOT NULL,
    customer_id BIGINT      NOT NULL REFERENCES customer (id),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);