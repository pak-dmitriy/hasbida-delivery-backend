package com.delivery.habsida.dto;

public record EmployeeCreateResponse (
        Long id,
        String userName,
        String email,
        String firstName,
        String lastName,
        String phone,
        String role

) {}

