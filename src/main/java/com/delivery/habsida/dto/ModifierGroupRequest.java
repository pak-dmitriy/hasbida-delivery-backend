package com.delivery.habsida.dto;

import jakarta.validation.constraints.*;

public record ModifierGroupRequest(

        @NotBlank(message = "Name can not be empty")
        @Size(max = 50, message = "Name must be at most 50")
        String name,

        boolean required,

        @NotNull
        @Min(value = 0, message = "MinSelect can not be negative")
        Integer minSelect,

        @NotNull()
        @Positive(message = "MaxSelect must be greater than zero")
        Integer maxSelect
) {
}
