package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.models.User;

import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = UserController.class)
@Import(WebSecurityConfig.class)
public class UserControllerTest {

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
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        // Test data setup
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@test.com");
        user.setPassword("password123");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@test.com");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password123")
                .build();
    }

    @Test
    public void testFindById_Success() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When & Then
        mockMvc.perform(get("/api/user/1")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@test.com"))
                .andExpect(jsonPath("$.admin").value(false));

        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    public void testFindById_UserNotFound() throws Exception {
        // Given
        when(userService.findById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/user/999")
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        verify(userService).findById(999L);
    }

    @Test
    public void testFindById_InvalidIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/user/invalid")
                .with(user(userDetails)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(user);
        doNothing().when(userService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/user/1")
                .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        // Given
        when(userService.findById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/api/user/999")
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        verify(userService).findById(999L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteUser_Unauthorized() throws Exception {
        // Given
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setEmail("different@test.com");
        
        when(userService.findById(2L)).thenReturn(differentUser);

        // When & Then
        mockMvc.perform(delete("/api/user/2")
                .with(user(userDetails)))
                .andExpect(status().isUnauthorized());

        verify(userService).findById(2L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteUser_InvalidIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/user/invalid")
                .with(user(userDetails)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteUser_SameEmailSuccess() throws Exception {
        // Given
        User sameUser = new User();
        sameUser.setId(1L);
        sameUser.setEmail("john.doe@test.com"); // Same email as userDetails
        
        when(userService.findById(1L)).thenReturn(sameUser);
        doNothing().when(userService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/user/1")
                .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    public void testFindById_LargeId() throws Exception {
        // Given
        Long largeId = Long.MAX_VALUE;
        when(userService.findById(largeId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/user/" + largeId)
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        verify(userService).findById(largeId);
    }

    @Test
    public void testDeleteUser_LargeId() throws Exception {
        // Given
        Long largeId = Long.MAX_VALUE;
        when(userService.findById(largeId)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/api/user/" + largeId)
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        verify(userService).findById(largeId);
    }
}