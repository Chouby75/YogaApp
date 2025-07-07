package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthController.class)
@Import(WebSecurityConfig.class) // Import de la configuration de sécurité
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

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
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L, 
            "test@test.com", 
            "firstName", 
            "lastName", 
            false, 
            "password"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        // On donne les scripts à nos clones
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication))
            .thenReturn("mocked-jwt-token");

        // 2. ACT & ASSERT
        // Le robot envoie la requête POST et on vérifie la réponse
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf())) // Gestion du CSRF pour les tests
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
            .andExpect(jsonPath("$.type").value("Bearer")) // Vérification du type de token
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("test@test.com"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // 1. ARRANGE (Préparation)
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password123");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/auth/register") // On fait un POST sur l'URL
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)) // On envoie la requête complète
                .with(csrf())) // Ne pas oublier la protection CSRF
            .andExpect(status().isOk()) // On s'attend à un statut 200 OK
            .andExpect(jsonPath("$.message").value("User registered successfully!")); // Et au bon message
    }

    @Test 
    public void testAuthenticateUser_MissingEmail() throws Exception {
        // Test avec email manquant
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthenticateUser_MissingPassword() throws Exception {
        // Test avec mot de passe manquant
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }
}