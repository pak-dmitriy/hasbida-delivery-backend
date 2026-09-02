
package com.delivery.habsida.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;


    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService);
        SecurityContextHolder.clearContext();
    }


    @Test
    void shouldContinueWithoutAuthentication_whenJwtIsMissing() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }


    @Test
    void shouldContinueWithoutAuthentication_whenJwtIsInvalid() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer invalid-token");

        when(jwtService.extractEmail("invalid-token"))
                .thenThrow(new RuntimeException("Invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService).extractEmail("invalid-token");
        verify(filterChain).doFilter(request, response);
    }


    @Test
    void shouldContinueWithoutAuthentication_whenJwtIsExpired() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer expired-token");

        when(jwtService.extractEmail("expired-token"))
                .thenThrow(new RuntimeException("JWT expired"));

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService).extractEmail("expired-token");
        verify(filterChain).doFilter(request, response);
    }
}

