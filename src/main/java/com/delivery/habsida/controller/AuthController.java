package com.delivery.habsida.controller;

import com.delivery.habsida.dto.LoginRequest;

import com.delivery.habsida.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}



