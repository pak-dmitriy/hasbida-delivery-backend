package com.delivery.habsida.dto;

import com.delivery.habsida.entity.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest (
        @NotBlank(message = "Name can not be empty")
        @Size(max = 50, message = "Name must be at most 50")
        String name,

        @NotBlank(message = "Description can not be empty")
        @Size(max = 50, message = "Name must be at most 50")
        String description,

        @NotNull(message = "Price can not be empty")
        BigDecimal price,

        @NotNull
        @Min(value = 0, message = "Stock can not be negative")
        Integer stock,

        @NotNull(message = "LowStockThreshold can not be empty")
        @Min(value = 0, message = "LowStockThreshold must be greater than zero")
        Integer lowStockThreshold,

        @NotNull(message = "Status can not be empty")
        ProductStatus status,

        @NotNull
        @Positive(message = "MaxQuantity must be greater than zero")
        Integer maxQuantity,

        @NotNull
        @Positive(message = "MinQuantity must be greater than zero")
        Integer minQuantity,

        @NotNull(message = "CategoryId can not be empty")
        Long categoryId

)
{
}
