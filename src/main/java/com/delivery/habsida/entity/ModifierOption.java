package com.delivery.habsida.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "modifier_options")
public class ModifierOption extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price_delta", nullable = false)
    private BigDecimal priceDelta;

    @Column(name = "is_free", nullable = false)
    private boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_group_id", nullable = false)
    private ModifierGroup modifierGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceDelta() {
        return priceDelta;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public void setPriceDelta(BigDecimal priceDelta) {
        this.priceDelta = priceDelta;
    }

    public ModifierGroup getModifierGroup() {
        return modifierGroup;
    }

    public void setModifierGroup(ModifierGroup modifierGroup) {
        this.modifierGroup = modifierGroup;
    }
}
