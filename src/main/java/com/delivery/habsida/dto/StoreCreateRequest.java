package com.delivery.habsida.dto;

import com.delivery.habsida.entity.Status;
import com.delivery.habsida.entity.TypeStoreServices;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
                Status status

) {
}
