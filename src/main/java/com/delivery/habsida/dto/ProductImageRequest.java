package com.delivery.habsida.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductImageRequest(

        @NotBlank(message = "ImagePhoto cannot be empty")
        @Size(max = 255, message = "ImagePhoto must be at most 255 characters")
        String imagePhoto,

        @NotNull(message = "SortOrder cannot be empty ")
        @Min(value = 0, message = "SortOrder cannot be negative")
        Integer sortOrder
){
}
