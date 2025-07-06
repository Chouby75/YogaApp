package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// On charge uniquement la couche Web pour AuthController
// @WebMvcTest(controllers = AuthController.class)
@SpringBootTest
@Import(WebSecurityConfig.class)
public class AuthControllerTest {

    // On injecte le robot-client
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // 1. ARRANGE
        // On prépare notre fausse requête de login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        // On prépare les fausses données de l'utilisateur authentifié
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "firstName", "lastName", false, "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        
        // On donne les scripts à nos clones
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked-jwt-token");

        // 2. ACT & ASSERT
        // Le robot envoie la requête POST et on vérifie la réponse
        mockMvc.perform(post("/api/auth/login") // On fait un POST sur l'URL
                .contentType(MediaType.APPLICATION_JSON) // On dit que le corps est en JSON
                .content(objectMapper.writeValueAsString(loginRequest))) // On met notre objet requête dans le corps
            .andExpect(status().isOk()) // On s'attend à un statut 200 OK
            .andExpect(jsonPath("$.token").value("mocked-jwt-token")); // On vérifie que le JSON de réponse contient bien notre token
    }
}