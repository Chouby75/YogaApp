package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user1;
    private User user2;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        
        // Setup Teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        // Setup Users
        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setEmail("alice@test.com");
        user1.setCreatedAt(now);
        user1.setUpdatedAt(now);

        user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setEmail("bob@test.com");
        user2.setCreatedAt(now);
        user2.setUpdatedAt(now);

        // Setup Session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Relaxing yoga session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        // Setup SessionDto
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Relaxing yoga session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(now);
        sessionDto.setUpdatedAt(now);
    }

    @Test
    public void testToDto_Success() {
        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(session.getId());
        assertThat(result.getName()).isEqualTo(session.getName());
        assertThat(result.getDescription()).isEqualTo(session.getDescription());
        assertThat(result.getDate()).isEqualTo(session.getDate());
        assertThat(result.getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(result.getUsers()).containsExactly(1L, 2L);
        assertThat(result.getCreatedAt()).isEqualTo(session.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(session.getUpdatedAt());
    }

    @Test
    public void testToDto_WithNullTeacher() {
        // Given
        session.setTeacher(null);

        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTeacher_id()).isNull();
        assertThat(result.getName()).isEqualTo(session.getName());
        assertThat(result.getDescription()).isEqualTo(session.getDescription());
    }

    @Test
    public void testToDto_WithNullUsers() {
        // Given
        session.setUsers(null);

        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).isEmpty();
        assertThat(result.getName()).isEqualTo(session.getName());
        assertThat(result.getDescription()).isEqualTo(session.getDescription());
    }

    @Test
    public void testToDto_WithEmptyUsers() {
        // Given
        session.setUsers(Collections.emptyList());

        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).isEmpty();
        assertThat(result.getName()).isEqualTo(session.getName());
        assertThat(result.getDescription()).isEqualTo(session.getDescription());
    }

    @Test
    public void testToEntity_Success() {
        // Given
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(sessionDto.getId());
        assertThat(result.getName()).isEqualTo(sessionDto.getName());
        assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(result.getDate()).isEqualTo(sessionDto.getDate());
        assertThat(result.getTeacher()).isEqualTo(teacher);
        assertThat(result.getUsers()).hasSize(2);
        assertThat(result.getUsers()).containsExactly(user1, user2);
        assertThat(result.getCreatedAt()).isEqualTo(sessionDto.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(sessionDto.getUpdatedAt());
    }

    @Test
    public void testToEntity_WithNullTeacherId() {
        // Given
        sessionDto.setTeacher_id(null);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTeacher()).isNull();
        assertThat(result.getName()).isEqualTo(sessionDto.getName());
        assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    public void testToEntity_WithNullUsers() {
        // Given
        sessionDto.setUsers(null);
        when(teacherService.findById(1L)).thenReturn(teacher);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).isEmpty();
        assertThat(result.getName()).isEqualTo(sessionDto.getName());
        assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    public void testToEntity_WithEmptyUsers() {
        // Given
        sessionDto.setUsers(Collections.emptyList());
        when(teacherService.findById(1L)).thenReturn(teacher);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).isEmpty();
        assertThat(result.getName()).isEqualTo(sessionDto.getName());
        assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    public void testToEntity_WithNonExistentUser() {
        // Given
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(null); // User not found

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).hasSize(2);
        assertThat(result.getUsers()).containsExactly(user1, null);
    }

    @Test
    public void testToEntity_WithNonExistentTeacher() {
        // Given
        when(teacherService.findById(1L)).thenReturn(null);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTeacher()).isNull();
        assertThat(result.getUsers()).hasSize(2);
        assertThat(result.getUsers()).containsExactly(user1, user2);
    }

    @Test
    public void testToDto_WithSingleUser() {
        // Given
        session.setUsers(Arrays.asList(user1));

        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).hasSize(1);
        assertThat(result.getUsers()).containsExactly(1L);
    }

    @Test
    public void testToEntity_WithSingleUser() {
        // Given
        sessionDto.setUsers(Arrays.asList(1L));
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).hasSize(1);
        assertThat(result.getUsers()).containsExactly(user1);
    }

    @Test
    public void testBidirectionalMapping() {
        // Given
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // When - Convert to DTO and back to Entity
        SessionDto dto = sessionMapper.toDto(session);
        Session entityFromDto = sessionMapper.toEntity(dto);

        // Then
        assertThat(entityFromDto.getId()).isEqualTo(session.getId());
        assertThat(entityFromDto.getName()).isEqualTo(session.getName());
        assertThat(entityFromDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(entityFromDto.getDate()).isEqualTo(session.getDate());
        assertThat(entityFromDto.getTeacher().getId()).isEqualTo(session.getTeacher().getId());
        assertThat(entityFromDto.getUsers()).hasSize(session.getUsers().size());
        assertThat(entityFromDto.getCreatedAt()).isEqualTo(session.getCreatedAt());
        assertThat(entityFromDto.getUpdatedAt()).isEqualTo(session.getUpdatedAt());
    }

    
}