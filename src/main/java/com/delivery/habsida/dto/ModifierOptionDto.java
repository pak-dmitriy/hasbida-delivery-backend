package com.delivery.habsida.dto;

import com.delivery.habsida.entity.ModifierOption;

import java.math.BigDecimal;

public record ModifierOptionDto(

        Long id,
        Long modifierGroupId,
        String name,
        BigDecimal priceDelta,
        boolean isFree

) {
    public static ModifierOptionDto from(ModifierOption option) {
        return new ModifierOptionDto(
                option.getId(),
                option.getModifierGroup().getId(),
                option.getName(),
                option.getPriceDelta(),
                option.isFree()
        );
    }
}
