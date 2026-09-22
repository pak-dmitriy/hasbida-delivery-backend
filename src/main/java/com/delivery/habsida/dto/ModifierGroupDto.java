package com.delivery.habsida.dto;

import com.delivery.habsida.entity.ModifierGroup;

public record ModifierGroupDto(

        Long id,
        Long storeId,
        String name,
        boolean required,
        Integer minSelect,
        Integer maxSelect

) {
    public static ModifierGroupDto from (ModifierGroup modifierGroup) {
return new ModifierGroupDto(
        modifierGroup.getId(),
        modifierGroup.getStore().getId(),
        modifierGroup.getName(),
        modifierGroup.isRequired(),
        modifierGroup.getMinSelect(),
        modifierGroup.getMaxSelect()
);
    }
}
