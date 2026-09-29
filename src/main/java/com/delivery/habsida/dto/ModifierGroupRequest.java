package com.delivery.habsida.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public record ModifierGroupRequest(

        @NotBlank(message = "Name can not be empty")
        @Size(max = 50, message = "Name must be at most 50")
        String name,

        @NotNull(message = "Required can not be empty")
        Boolean required,

        @NotNull
        @Min(value = 0, message = "MinSelect can not be negative")
        Integer minSelect,

        @NotNull()
        @Positive(message = "MaxSelect must be greater than zero")
        Integer maxSelect
) {
    @JsonIgnore
    @AssertTrue(message = "minSelect or required must be valid")
    public boolean isMinSelectValid() {
        if (Boolean.TRUE.equals(required) && minSelect < 1) {
            return false;
        }
        return true;
    }
}
