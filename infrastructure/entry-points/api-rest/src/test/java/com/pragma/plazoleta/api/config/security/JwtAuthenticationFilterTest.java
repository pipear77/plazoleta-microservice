package com.pragma.plazoleta.api.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private final String secret = "una-clave-secreta-muy-larga-y-segura-para-firmar-el-token-123456";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "secret", secret);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        // Limpiamos el contexto antes de cada prueba para evitar falsos positivos
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthHeader_ContinuesFilterChain() throws ServletException, IOException {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidAuthHeaderPrefix_ContinuesFilterChain() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic token-invalido-123");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Generamos un token real firmado con la misma clave secreta de la prueba
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        String token = Jwts.builder()
                .subject("propietario@test.com")
                .claim("role", "ROLE_PROPIETARIO")
                .claim("id", 1L)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("propietario@test.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals(1L, SecurityContextHolder.getContext().getAuthentication().getDetails());
    }

    @Test
    void doFilterInternal_InvalidToken_ReturnsUnauthorized() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer token.falso.inventado");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertEquals("Token inválido o expirado", response.getErrorMessage());
        verify(filterChain, never()).doFilter(request, response); // Verifica que la petición no continuó
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}