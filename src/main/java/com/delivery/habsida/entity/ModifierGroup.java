package com.delivery.habsida.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "modifier_groups")
public class ModifierGroup extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "min_Select", nullable = false)
    private int minSelect;

    @Column(name = "max_Select", nullable = false)
    private int maxSelect;

    @OneToMany(mappedBy = "modifierGroup")
    private List<ProductModifierGroup> productModifierGroups;

    @OneToMany(mappedBy = "modifierGroup")
    private List<ModifierOption> modifierOptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getMinSelect() {
        return minSelect;
    }

    public void setMinSelect(int minSelect) {
        this.minSelect = minSelect;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public List<ProductModifierGroup> getProductModifierGroup() {
        return productModifierGroups;
    }

    public void setProductModifierGroup(List<ProductModifierGroup> productModifierGroups) {
        this.productModifierGroups = productModifierGroups;
    }

    public List<ModifierOption> getModifierOption() {
        return modifierOptions;
    }

    public void setModifierOption(List<ModifierOption> modifierOptions) {
        this.modifierOptions = modifierOptions;
    }

    public Store getStore() {return store;}

    public void setStore(Store store) {this.store = store;}
}
