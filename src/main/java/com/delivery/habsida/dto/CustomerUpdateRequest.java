package com.delivery.habsida.dto;

import com.delivery.habsida.entity.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest (
        @NotBlank(message = "Name should not be empty")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Phone should not be empty")
        @Size(max = 20, message = "Phone must be at most 20 characters")
        String phone,

        @NotNull(message = "Status should not be empty")
        CustomerStatus status
){
}