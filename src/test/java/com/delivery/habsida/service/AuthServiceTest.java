package com.delivery.habsida.service;

import com.delivery.habsida.dto.JwtResponse;
import com.delivery.habsida.dto.LoginRequest;
import com.delivery.habsida.entity.Role;
import com.delivery.habsida.entity.User;
import com.delivery.habsida.entity.UserRole;
import com.delivery.habsida.exception.InvalidCredentialsException;
import com.delivery.habsida.repository.UserRepository;
import com.delivery.habsida.repository.UserRoleRepository;
import com.delivery.habsida.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private AuthService authService;

   private LoginRequest request;
   private User user;

   @BeforeEach
   void setUp() {
       request = new LoginRequest("test@test.com", "12345");
       user = new User();
       user.setId(1L);
       user.setEmail("test@test.com");
       user.setPassword("hashedPassword");
   }
   @Test
    void login_shouldReturnJwtResponse_whenCredentialsAreValid() {

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(eq(1L),eq("test@test.com"), anyList())).thenReturn("fakeToken");

       Role role = new Role();
       role.setName("ADMIN");

       UserRole userRole = new UserRole();
       userRole.setRole(role);

       when(userRoleRepository.findByUser(user)).thenReturn(List.of(userRole));

       JwtResponse response = authService.login(request);

        assertEquals("fakeToken", response.token());
        assertEquals("Bearer", response.tokenType());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
       when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

       assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_shouldThrowException_whenPasswordIsIncorrect() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "hashedPassword")).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

    }

}