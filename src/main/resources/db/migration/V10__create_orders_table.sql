CREATE TABLE orders
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    store_id            BIGINT         NOT NULL REFERENCES store (id),
    customer_id         BIGINT         NOT NULL REFERENCES customer (id),
    customer_address_id BIGINT         NOT NULL REFERENCES customer_address (id),
    order_number        VARCHAR(20)    NOT NULL,
    type                VARCHAR(20)    NOT NULL CHECK (type IN ('DELIVERY', 'PICKUP')),
    status              VARCHAR(20)    NOT NULL CHECK (status IN
                                                       ('CREATED', 'ACCEPTED', 'CANCELLED', 'REJECTED', 'IN_PROGRESS',
                                                        'COMPLETED')),
    customer_note       VARCHAR(500),
    subtotal            NUMERIC(10, 2) NOT NULL,
    delivery_fee        NUMERIC(10, 2) NOT NULL DEFAULT 0,
    discount_total      NUMERIC(10, 2) NOT NULL DEFAULT 0,
    total               NUMERIC(10, 2) NOT NULL,
    currency            VARCHAR(3)     NOT NULL DEFAULT 'KRW',
    accepted_at         TIMESTAMPTZ,
    cancelled_at        TIMESTAMPTZ,
    cancellation_reason VARCHAR(255),
    rejected_at         TIMESTAMPTZ,
    reject_reason       VARCHAR(255),
    created_at          TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    UNIQUE (store_id, order_number)
)