package com.delivery.habsida.service;

import com.delivery.habsida.dto.JwtResponse;
import com.delivery.habsida.dto.LoginRequest;
import com.delivery.habsida.entity.User;
import com.delivery.habsida.exception.InvalidCredentialsException;
import com.delivery.habsida.repository.UserRepository;
import com.delivery.habsida.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }
        String token = jwtService.generateToken(loginRequest.email());
        return new JwtResponse(token, "Bearer");

    }

}
