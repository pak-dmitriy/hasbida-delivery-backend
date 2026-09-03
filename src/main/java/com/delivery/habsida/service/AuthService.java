package com.delivery.habsida.service;

import com.delivery.habsida.dto.JwtResponse;
import com.delivery.habsida.dto.LoginRequest;
import com.delivery.habsida.entity.User;
import com.delivery.habsida.entity.UserRole;
import com.delivery.habsida.exception.InvalidCredentialsException;
import com.delivery.habsida.repository.UserRepository;
import com.delivery.habsida.repository.UserRoleRepository;
import com.delivery.habsida.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRoleRepository userRoleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRoleRepository = userRoleRepository;
    }


    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        if (userRoles.isEmpty()) throw new InvalidCredentialsException("User has no roles assigned");

        List<String> roles = userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();

        String token = jwtService.generateToken(loginRequest.email(), roles);
        return new JwtResponse(token, "Bearer");

    }

}
