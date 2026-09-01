package com.delivery.habsida.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email should not be empty")
    @Email
    private String email;
    @NotBlank(message = "Password should not be empty")
    private String password;
}
