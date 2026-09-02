package com.delivery.habsida.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    @Test
    public void shouldGenerateToken() {
        String token = jwtService.generateToken("test@test.com");
        System.out.println("token: " + token);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void extractEmailShouldReturnOriginalEmail() {
        String token2 = jwtService.generateToken("test2@test.com");
        String email = jwtService.extractEmail(token2);
        assertEquals("test2@test.com", email);
    }
}
