package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = SessionController.class)
@Import(WebSecurityConfig.class) // Import de la configuration de sécurité
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

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

    private Session session;
    private SessionDto sessionDto;
    private List<Session> sessions;
    private List<SessionDto> sessionDtos;

    @BeforeEach
    public void setUp() {
        // Données de test
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        session = new Session();
        session.setId(1L);
        session.setName("Session Yoga");
        session.setDescription("Description de la session");
        session.setDate(new Date());
        session.setTeacher(teacher);

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session Yoga");
        sessionDto.setDescription("Description de la session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        sessions = Arrays.asList(session);
        sessionDtos = Arrays.asList(sessionDto);
    }

    @Test
    @WithMockUser
    public void testFindAll_Success() throws Exception {
        // ARRANGE
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // ACT & ASSERT
        mockMvc.perform(get("/api/session"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Session Yoga"));
    }

    @Test
    @WithMockUser
    public void testFindById_Success() throws Exception {
        // ARRANGE
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // ACT & ASSERT
        mockMvc.perform(get("/api/session/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Session Yoga"));
    }

    @Test
    @WithMockUser
    public void testFindById_NotFound() throws Exception {
        // ARRANGE
        when(sessionService.getById(999L)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(get("/api/session/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testFindById_InvalidId() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/session/invalid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testCreate_Success() throws Exception {
        // ARRANGE
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // ACT & ASSERT
        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Session Yoga"));
    }

    @Test
    @WithMockUser
    public void testCreate_ValidationError() throws Exception {
        // ARRANGE - SessionDto invalide (sans nom)
        SessionDto invalidSessionDto = new SessionDto();
        invalidSessionDto.setDescription("Description");

        // ACT & ASSERT
        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSessionDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testUpdate_Success() throws Exception {
        // ARRANGE
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // ACT & ASSERT
        mockMvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Session Yoga"));
    }

    @Test
    @WithMockUser
    public void testUpdate_InvalidId() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(put("/api/session/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testDelete_Success() throws Exception {
        // ARRANGE
        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/session/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDelete_NotFound() throws Exception {
        // ARRANGE
        when(sessionService.getById(999L)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/session/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDelete_InvalidId() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(delete("/api/session/invalid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testParticipate_Success() throws Exception {
        // ARRANGE
        doNothing().when(sessionService).participate(1L, 1L);

        // ACT & ASSERT
        mockMvc.perform(post("/api/session/1/participate/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testParticipate_InvalidIds() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(post("/api/session/invalid/participate/invalid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipate_Success() throws Exception {
        // ARRANGE
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/session/1/participate/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipate_InvalidIds() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(delete("/api/session/invalid/participate/invalid"))
            .andExpect(status().isBadRequest());
    }

}