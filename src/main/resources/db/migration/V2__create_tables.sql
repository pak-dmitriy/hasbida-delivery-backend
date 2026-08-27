CREATE TABLE users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name  VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE store
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR(50)                                                      NOT NULL UNIQUE,
    store_slug          VARCHAR(50)                                                      NOT NULL UNIQUE,
    description         VARCHAR(255)                                                     NOT NULL,
    type_store_services VARCHAR(50) CHECK (type_store_services IN
                                           ('FAST_FOOD', 'BAKERY', 'RESTAURANT'))        NOT NULL,
    phone               VARCHAR(20)                                                      NOT NULL,
    logo                VARCHAR(255)                                                     NOT NULL,
    pickup_address      VARCHAR(255)                                                     NOT NULL,
    status              VARCHAR(50) CHECK (status IN
                                           ('ACTIVE', 'MODERATION', 'FROZEN', 'CLOSED')) NOT NULL,
    created_at          TIMESTAMPTZ                                                      NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ                                                      NOT NULL DEFAULT NOW()
);

CREATE TABLE user_role
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_id    BIGINT      NOT NULL REFERENCES role (id),
    user_id    BIGINT      NOT NULL REFERENCES users (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (role_id, user_id)
);

CREATE TABLE user_store_access
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id),
    store_id   BIGINT      NOT NULL REFERENCES store (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, store_id)
);