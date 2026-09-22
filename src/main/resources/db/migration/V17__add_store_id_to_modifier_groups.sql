ALTER TABLE modifier_groups
    ADD COLUMN store_id BIGINT NOT NULL;

ALTER TABLE modifier_groups
    ADD CONSTRAINT modifier_groups_store_id_fkey
        FOREIGN KEY (store_id) REFERENCES store (id);

CREATE INDEX idx_modifier_groups_store_id ON modifier_groups (store_id);