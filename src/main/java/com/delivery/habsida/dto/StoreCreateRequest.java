package com.delivery.habsida.dto;

import com.delivery.habsida.entity.Status;
import com.delivery.habsida.entity.TypeStoreServices;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record StoreCreateRequest(

        @NotBlank(message = "The name is required")
        String name,

        @NotBlank(message = "The slug is required")
        String storeSlug,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Type of services are required")
        TypeStoreServices typeStoreServices,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotBlank(message = "Logo cant be blank")
        String logo,

        @NotBlank(message = "Address is required")
        String pickupAddress,

        @NotNull
                Status status,

        @NotNull(message = "Delivery fee is required")
        @PositiveOrZero(message = "Delivery fee can not be negative")
        @Digits(integer = 8, fraction = 2, message = "Delivery fee must have up to 8 digits and 2 decimals")
        BigDecimal deliveryFee

) {
}
