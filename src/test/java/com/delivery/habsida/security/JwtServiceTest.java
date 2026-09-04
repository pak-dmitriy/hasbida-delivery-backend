package com.delivery.habsida.security;

import com.delivery.habsida.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    @Test
    public void shouldGenerateToken() {
        String token = jwtService.generateToken( 1L,"test@test.com", List.of("ADMIN"));
        System.out.println("token: " + token);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void extractEmailShouldReturnOriginalEmail() {
        String token2 = jwtService.generateToken(2L,"test2@test.com", List.of("ADMIN"));
        String email = jwtService.extractEmail(token2);
        assertEquals("test2@test.com", email);
    }
}
