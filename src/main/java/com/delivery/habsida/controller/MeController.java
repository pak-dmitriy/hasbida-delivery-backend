package com.delivery.habsida.controller;

import com.delivery.habsida.dto.MeResponse;
import com.delivery.habsida.service.AuthService;
import com.delivery.habsida.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {
    private final UserService userService;

    public MeController(UserService userService, AuthService authService) {
        this.userService = userService;
    }

    @GetMapping("/api/auth/me")
    public MeResponse authMe(Authentication authentication) {
        return userService.getMe(authentication);
    }
}
