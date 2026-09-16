package com.delivery.habsida.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeCreateRequest(

        @NotBlank(message = "UserName cant be blank")
        String userName,

        @Email
        @NotBlank(message = "Email cant be blank")
        String email,

        @NotBlank(message = "Password cant be blank")
        String password,

        @NotBlank(message = "firstName cant be blank")
        String firstName,

        @NotBlank(message = "lastName cant be blank")
        String lastName,

        @NotBlank(message = "phone cant be blank")
        String phone,

        @NotBlank(message = "Role cant be blank")
        String role

) {
}
