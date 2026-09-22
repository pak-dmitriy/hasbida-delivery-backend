package com.delivery.habsida.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ModifierOptionRequest(

        @NotBlank(message = "Name can not be empty")
        String name,

        @NotNull(message = "PriceDelta can not be empty")
        @DecimalMin(value = "0.0", message = "PriceDelta can not be negative")
        BigDecimal priceDelta,

        @NotNull(message = "isFree can not be empty")
        Boolean isFree
) {
}
