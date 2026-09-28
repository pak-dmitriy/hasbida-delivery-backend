package com.delivery.habsida.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item_modifiers")
public class OrderItemModifier extends BaseEntity {

    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "price_delta", nullable = false)
    private BigDecimal priceDelta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_option_id", nullable = false)
    private ModifierOption modifierOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ModifierOption getModifierOption() {
        return modifierOption;
    }

    public void setModifierOption(ModifierOption modifierOption) {
        this.modifierOption = modifierOption;
    }

    public BigDecimal getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(BigDecimal priceDelta) {
        this.priceDelta = priceDelta;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
