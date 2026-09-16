package com.delivery.habsida.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImagesRequest (

        @NotBlank(message = "ImagePhoto cannot be empty")
        String imagePhoto,

        @NotNull(message = "SortOrder cannot be empty ")
        @Min(value = 0, message = "SortOrder cannot be negative")
        Integer sortOrder
){
}
