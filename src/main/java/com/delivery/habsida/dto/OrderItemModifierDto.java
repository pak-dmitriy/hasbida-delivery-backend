package com.delivery.habsida.dto;

import com.delivery.habsida.entity.OrderItemModifier;

import java.math.BigDecimal;

public record OrderItemModifierDto(
        Long id,
        String optionName,
        BigDecimal priceDelta
        ) {
    public static OrderItemModifierDto from(OrderItemModifier orderItemModifier) {
        return new OrderItemModifierDto(
                orderItemModifier.getId(),
                orderItemModifier.getOptionName(),
                orderItemModifier.getPriceDelta()
        );
    }
}
