create table product_images
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    image_photo VARCHAR(255) not null,
    product_id BIGINT NOT NULL REFERENCES products(id),
    sort_order int not null,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
)