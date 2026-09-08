package com.delivery.habsida.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "Name should not be empty")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Description should not be empty")
        @Size(max = 50, message = "Description must be at most 50 characters")
        String description,

        @NotBlank(message = "Category photo should not be empty")
        String categoryPhoto

) {
}
