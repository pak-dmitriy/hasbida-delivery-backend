package com.delivery.habsida.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRejectRequest (
    @NotBlank(message = "Reason should not be empty")
    String reason
){}
