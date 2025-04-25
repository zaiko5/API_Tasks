package com.taskList.Auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testeo para la clase JWTAuthenticationFilter
 */
@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    //Dependencias de la clase a testear
    @Mock
    private JWTService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    //Inyectando la clase a testear
    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad antes de cada prueba
    }

    /**
     * Testeos para el metodo doInternalFilter (casi nunca cambian).
     */
    @Test
    void doFilterInternal_publicRoutes_shouldContinueFilterChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/login");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);

        when(request.getRequestURI()).thenReturn("/signin");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(2)).doFilter(request, response); // Se llama una vez más
    }

    @Test
    void doFilterInternal_noAuthHeader_shouldThrowJwtExceptionAndSetAttribute() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(JwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(request).setAttribute("customErrorMessage", "Falta el token o es incorrecto");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidAuthHeaderFormat_shouldThrowJwtExceptionAndSetAttribute() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        assertThrows(JwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(request).setAttribute("customErrorMessage", "Falta el token o es incorrecto");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidTokenNoUsername_shouldThrowJwtExceptionAndSetAttribute() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.extraerUsername("invalid.token")).thenReturn(null);

        assertThrows(JwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(request).setAttribute("customErrorMessage", "Token inválido: no contiene información de usuario");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_usernameNotFound_shouldThrowAuthenticationExceptionAndSetAttribute() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extraerUsername("valid.token")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(AuthenticationException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(request).setAttribute("customErrorMessage", "Usuario no encontrado: User not found");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidTokenSignature_shouldThrowJwtExceptionAndSetAttribute() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.extraerUsername("invalid.token")).thenReturn("testuser");
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.validarToken("invalid.token", userDetails)).thenReturn(false);

        assertThrows(JwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(request).setAttribute("customErrorMessage", "Token inválido o manipulado");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validToken_shouldAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extraerUsername("valid.token")).thenReturn("testuser");
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.validarToken("valid.token", userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testuser", authentication.getName());
        assertTrue(authentication.getPrincipal() instanceof UserDetails);
        assertTrue(authentication.getAuthorities().isEmpty());
    }
}