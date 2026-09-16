package com.delivery.habsida.dto;

import com.delivery.habsida.entity.ProductImages;

public record ProductImagesDto(
        Long id,
        String imagePhoto,
        int sortOrder
) {

    public static ProductImagesDto from(ProductImages productImages) {
        return  new ProductImagesDto(
                productImages.getId(),
                productImages.getImagePhoto(),
                productImages.getSortOrder()
        );
    }
}
