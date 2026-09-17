package com.delivery.habsida.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_modifier_groups")
public class ProductModifierGroup extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "modifier_group_id")
    private ModifierGroup modifierGroup;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ModifierGroup getModifierGroup() {
        return modifierGroup;
    }

    public void setModifierGroup(ModifierGroup modifierGroup) {
        this.modifierGroup = modifierGroup;
    }
}
