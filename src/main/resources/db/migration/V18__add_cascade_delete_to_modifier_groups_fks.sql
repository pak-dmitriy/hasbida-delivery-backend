ALTER TABLE modifier_options DROP CONSTRAINT modifier_options_modifier_group_id_fkey;

ALTER TABLE modifier_options ADD CONSTRAINT modifier_options_modifier_group_id_fkey
    FOREIGN KEY (modifier_group_id) REFERENCES modifier_groups (id) ON DELETE CASCADE;

ALTER TABLE product_modifier_groups DROP CONSTRAINT product_modifier_groups_modifier_group_id_fkey;

ALTER TABLE product_modifier_groups ADD CONSTRAINT product_modifier_groups_modifier_group_id_fkey
    FOREIGN KEY (modifier_group_id) REFERENCES modifier_groups (id) ON DELETE CASCADE;

ALTER TABLE product_modifier_groups ADD CONSTRAINT uq_product_modifier_groups_product_group UNIQUE (product_id, modifier_group_id);



