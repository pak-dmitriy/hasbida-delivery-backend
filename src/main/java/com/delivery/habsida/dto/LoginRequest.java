package com.delivery.habsida.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



public record LoginRequest (
    @NotBlank(message = "Email should not be empty")
    @Email
     String email,
    @NotBlank(message = "Password should not be empty")
     String password
){}
