package com.delivery.habsida.dto;

import com.delivery.habsida.entity.ProductModifierGroup;

public record ProductModifierGroupDto(
        Long id,
        Long productId,
        Long modifierGroupId
) {
    public static ProductModifierGroupDto from(ProductModifierGroup productModifierGroup) {
        return new ProductModifierGroupDto(
                productModifierGroup.getId(),
                productModifierGroup.getProduct().getId(),
                productModifierGroup.getModifierGroup().getId()
        );
    }
}
