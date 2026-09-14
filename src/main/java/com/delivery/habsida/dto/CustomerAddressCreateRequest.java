package com.delivery.habsida.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerAddressCreateRequest(

        @NotBlank(message = "City should not be empty")
        @Size(max = 20, message = "City must be at most 20 characters")
        String city,

        @NotBlank(message = "Street should not be empty")
        @Size(max = 50, message = "Street must be at most 50 characters")
        String street,

        @NotBlank(message = "House should not be empty")
        @Size(max = 20, message = "House must be at most 20 characters")
        String house,

        @NotBlank(message = "Apartment should not be empty")
        @Size(max = 20, message = "Apartment must be at most 20 characters")
        String apartment
) {
}
