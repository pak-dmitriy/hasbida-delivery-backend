package com.delivery.habsida.dto;

import com.delivery.habsida.entity.Category;

public record CategoryDTO(
        Long id,
        String name,
        String description,
        String categoryPhoto,
        Long storeId
) {
    public static CategoryDTO from(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCategoryPhoto(),
                category.getStore().getId()
        );
    }


}

