package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        // Injection manuelle des dépendances mockées
        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsService);
    }

    @Test
    void testDoFilterInternal_InvalidJwtToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String invalidJwt = "invalid.jwt.token";
        String authHeader = "Bearer " + invalidJwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(invalidJwt)).thenReturn(false);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils).validateJwtToken(invalidJwt);
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_InvalidAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_EmptyAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("");

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(jwtUtils, never()).getUserNameFromJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_JwtUtilsThrowsException_ShouldContinueFilterChain() throws ServletException, IOException {
        // Arrange
        String validJwt = "valid.jwt.token";
        String authHeader = "Bearer " + validJwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(validJwt)).thenThrow(new RuntimeException("JWT validation error"));

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils).validateJwtToken(validJwt);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie à cause de l'exception
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_UserDetailsServiceThrowsException_ShouldContinueFilterChain() throws ServletException, IOException {
        // Arrange
        String validJwt = "valid.jwt.token";
        String username = "testuser";
        String authHeader = "Bearer " + validJwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(validJwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(validJwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("User not found"));

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils).validateJwtToken(validJwt);
        verify(jwtUtils).getUserNameFromJwtToken(validJwt);
        verify(userDetailsService).loadUserByUsername(username);
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie à cause de l'exception
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testParseJwt_ValidBearerToken_ExtractsTokenCorrectly() throws ServletException, IOException {
        // Arrange
        String jwt = "valid.jwt.token";
        String authHeader = "Bearer " + jwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils).validateJwtToken(jwt);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_BearerWithoutSpace_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearertoken123");

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(filterChain).doFilter(request, response);

        // Vérifier qu'aucune authentification n'a été définie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}