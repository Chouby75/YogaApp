package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private final String testJwtSecret = "testSecretKey";
    private final int testJwtExpirationMs = 86400000; // 24 hours
    private final String testUsername = "testuser@example.com";

    @BeforeEach
    void setUp() {
        // Set the private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", testJwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", testJwtExpirationMs);
    }

    private Authentication createMockAuthentication(String username) {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = org.mockito.Mockito.mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        return authentication;
    }

    @Test
    void shouldGenerateJwtToken() {
        // Given
        Authentication authentication = createMockAuthentication(testUsername);

        // When
        String token = jwtUtils.generateJwtToken(authentication);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts: header.payload.signature
        
        // Verify token content
        Claims claims = Jwts.parser()
                .setSigningKey(testJwtSecret)
                .parseClaimsJws(token)
                .getBody();
        
        assertThat(claims.getSubject()).isEqualTo(testUsername);
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getExpiration().getTime()).isGreaterThan(claims.getIssuedAt().getTime());
    }

    @Test
    void shouldExtractUsernameFromValidToken() {
        // Given
        Authentication authentication = createMockAuthentication(testUsername);
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Then
        assertThat(extractedUsername).isEqualTo(testUsername);
    }

    @Test
    void shouldValidateValidToken() {
        // Given
        Authentication authentication = createMockAuthentication(testUsername);
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectInvalidSignature() {
        // Given
        String tokenWithWrongSignature = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + testJwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        // When
        boolean isValid = jwtUtils.validateJwtToken(tokenWithWrongSignature);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectExpiredToken() {
        // Given
        String expiredToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired 1 second ago
                .signWith(SignatureAlgorithm.HS512, testJwtSecret)
                .compact();

        // When
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectMalformedToken() {
        // Given
        String malformedToken = "malformed.token.here";

        // When
        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectEmptyToken() {
        // When
        boolean isValid = jwtUtils.validateJwtToken("");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectNullToken() {
        // When
        boolean isValid = jwtUtils.validateJwtToken(null);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectTokenWithoutSignature() {
        // Given - Create a token without signature (only header and payload)
        String tokenWithoutSignature = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlckBleGFtcGxlLmNvbSIsImlhdCI6MTY0MzY0MDAwMCwiZXhwIjoxNjQzNzI2NDAwfQ";

        // When
        boolean isValid = jwtUtils.validateJwtToken(tokenWithoutSignature);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldHandleTokenWithInvalidCharacters() {
        // Given
        String invalidToken = "invalid!@#$%^&*()token";

        // When
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateTokenWithCorrectExpiration() {
        // Given
        Authentication authentication = createMockAuthentication(testUsername);

        // When
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Then
        Claims claims = Jwts.parser()
                .setSigningKey(testJwtSecret)
                .parseClaimsJws(token)
                .getBody();
        
        Date expirationDate = claims.getExpiration();
        Date issuedAtDate = claims.getIssuedAt();
        
        // Check that expiration is approximately jwtExpirationMs milliseconds after issuedAt
        long actualExpirationDuration = expirationDate.getTime() - issuedAtDate.getTime();
        assertThat(actualExpirationDuration).isEqualTo(testJwtExpirationMs);
        
        // Check that issuedAt is reasonable (within last few seconds)
        long now = System.currentTimeMillis();
        assertThat(issuedAtDate.getTime()).isLessThanOrEqualTo(now);
        assertThat(issuedAtDate.getTime()).isGreaterThan(now - 5000); // Within last 5 seconds
    }

    @Test
    void shouldGenerateUniqueTokensForSameUser() {
        // Given
        Authentication authentication = createMockAuthentication(testUsername);
        String token1 = jwtUtils.generateJwtToken(authentication);
        
        try {
            Thread.sleep(1000); // Ensure different timestamps (1 second)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String token2 = jwtUtils.generateJwtToken(authentication);

        // Then
        assertThat(token1).isNotEqualTo(token2);
        
        // But both should be valid and contain the same username
        assertThat(jwtUtils.validateJwtToken(token1)).isTrue();
        assertThat(jwtUtils.validateJwtToken(token2)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(token1)).isEqualTo(testUsername);
        assertThat(jwtUtils.getUserNameFromJwtToken(token2)).isEqualTo(testUsername);
    }

    @Test
    void shouldHandleDifferentUsernameFormats() {
        // Given - Test with different username formats
        String[] testUsernames = {
            "user@example.com",
            "user.name@domain.co.uk",
            "user123",
            "user_name",
            "user-name",
            "123user"
        };

        for (String username : testUsernames) {
            // Given
            Authentication authentication = createMockAuthentication(username);
            
            // When
            String token = jwtUtils.generateJwtToken(authentication);
            
            // Then
            assertThat(jwtUtils.validateJwtToken(token)).isTrue();
            assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo(username);
        }
    }

    @Test
    void shouldHandleUnsupportedJwtToken() {
        // Given - Create a token with an unsupported algorithm (this is tricky to test directly)
        // For this test, we'll create a token with a different structure
        String unsupportedToken = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ0ZXN0In0.";

        // When
        boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldExtractUsernameFromTokenWithSpecialCharacters() {
        // Given
        String specialUsername = "user@éxample.com";
        Authentication authentication = createMockAuthentication(specialUsername);
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Then
        assertThat(extractedUsername).isEqualTo(specialUsername);
    }

    @Test
    void shouldHandleTokenGenerationWithNullAuthentication() {
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtils.generateJwtToken(null);
        });
    }

    @Test
    void shouldHandleTokenGenerationWithNullUserDetails() {
        // Given
        Authentication authWithNullPrincipal = org.mockito.Mockito.mock(Authentication.class);
        when(authWithNullPrincipal.getPrincipal()).thenReturn(null);

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtils.generateJwtToken(authWithNullPrincipal);
        });
    }

    @Test
    void shouldHandleGetUsernameFromInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtils.getUserNameFromJwtToken(invalidToken);
        });
    }

    @Test
    void shouldHandleGetUsernameFromNullToken() {
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtils.getUserNameFromJwtToken(null);
        });
    }

    @Test
    void shouldHandleGetUsernameFromEmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtils.getUserNameFromJwtToken("");
        });
    }
}