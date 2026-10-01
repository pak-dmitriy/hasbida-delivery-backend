package com.delivery.habsida.controller;

import com.delivery.habsida.dto.JwtResponse;
import com.delivery.habsida.dto.LoginRequest;

import com.delivery.habsida.dto.MeResponse;
import com.delivery.habsida.security.UserPrincipal;
import com.delivery.habsida.service.AuthService;
import com.delivery.habsida.service.UserService;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getMe(principal.userId());
    }
}